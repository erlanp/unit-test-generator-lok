package com.areyoo.lok.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.areyoo.lok.service.api.WwService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ObjectUtils;

/**
 * @author xusong
 */
@ExtendWith(MockitoExtension.class)
class WwGenTest {
    /**
     * java文件夹， 用于生成测试的 when代码 可以自定义 比如 D:/java/lok/target/test-classes/../../src/
     */
    private static String filePath = "";

    /**
     * 生成 单元测试 InjectMocks 的变量名称
     */
    private String serviceName = "";

    private Boolean isSuperclass = false;

    // 是否生成私有方法的单元测试
    private Boolean genPrivateMethod = false;

    // 是否使用json 初始化对象
    private Boolean useJson = false;

    /**
     * 常用的 Exception
     */
    private Class importException = Exception.class;

    private String fileContent = "";

    private String importAny = "static org.mockito.ArgumentMatchers";

    @Test
    public void genTest() throws Exception {
        if ("".equals(filePath)) {
            // 反推java文件夹
            filePath = this.getClass().getResource("/").toString().substring(6) + "../../src/";
        }
        // 生成当前类的单元测试
        genCode(WwController.class, false);

        // 生成父类的单元测试
        // genCode(WwController.class.getSuperclass(), true);
    }

    private static String getAbsolutePath(Class myClass) {
        // 取得要生成单元测试的类的绝对地址 用于生成 when thenReturn
        String fileClassPath = myClass.getTypeName().replace(".", File.separator) + ".java";
        String path = getAbsolutePath(new File(filePath), fileClassPath);
        return path;
    }

    private static String getAbsolutePath(File file, String filePath) {
        File[] fs = file.listFiles();
        String result = "";
        for (File f : fs) {
            if(f.isDirectory()) {
                result = getAbsolutePath(f, filePath);
                if (!"".equals(result)) {
                    return result;
                }
            } else if(f.isFile() && f.getAbsolutePath().indexOf(filePath) > 0) {
                return f.getAbsolutePath();
            }
        }
        return "";
    }

    private Boolean isInit;
    private void genCode(Class myClass, Boolean isSuperclass) throws Exception {
        genCode(myClass, isSuperclass, true);
        genCode(myClass, isSuperclass, false);
    }

    private void genCode(Class myClass, Boolean isSuperclass, Boolean init) throws Exception {
        // 生成测试代码
        if ("java.lang.Object".equals(myClass.getTypeName())) {
            return;
        }
        if (init) {
            importSet = new HashSet<>(16);
            defaultMap = new HashMap<>(16);
        }
        isInit = init;
        this.isSuperclass = isSuperclass;
        String name = getType(myClass.getName());
        if ("".equals(serviceName)) {
            serviceName = name.substring(0, 1).toLowerCase() + name.substring(1);
        }

        setImport("org.mockito.InjectMocks");
        setImport("org.mockito.Mock");
        setImport("org.junit.Assert");
        setImport("org.junit.jupiter.api.Test");
        setImport("static org.mockito.Mockito.when");
        List<String> importList = new ArrayList<>(importSet);
        Collections.sort(importList);
        for (String importStr : importList) {
            println(importStr);
        }
        println("");

        Set<Field> fields = getDeclaredFields(myClass);

        List<String> lineList = readFileContent(myClass);
        fileContent = String.join("\n", lineList);

        Map<String, List<String>> map = new HashMap<>(16);

        println("class " + name + "Test {");

        println("@InjectMocks");
        println("private " + myClass.getSimpleName() + " " + serviceName + ";");
        println("");
        int number = 0;
        for (Field service : fields) {
            if (service.getAnnotations().length > 0 && !service.getType().getName().contains("java.") && service.getType().getName().contains(".")) {
                println("@Mock");
                setImport(service.getType().getName());
                println("private " + service.getType().getSimpleName() + " " + service.getName() + ";");
                println("");

                for (Method serviceMethod : getDeclaredMethods(service.getType(), true)) {
                    String methodStr = service.getName() + "." + serviceMethod.getName() + "(";
                    Type t = serviceMethod.getAnnotatedReturnType().getType();
                    if (!"void".equals(t.getTypeName()) && ("".equals(fileContent) || fileContent.indexOf(methodStr) > 0)) {
                        if (!map.containsKey(methodStr)) {
                            map.put(methodStr, new ArrayList<>(10));
                        }
                        map.get(methodStr).add(getWhen(serviceMethod, number, service.getName()));
                        number++;
                    }
                }
            }
        }

        Map<String, Set<List<String>>> whenMap = new HashMap<>(16);
        Map<String, Set<String>> whenMethod = new HashMap<>(16);
        Set<Method> methods = getDeclaredMethods(myClass, true);
        for (Method method : methods) {
            whenMap.put(method.getName(), new HashSet<>(10));
            whenMethod.put(method.getName(), new HashSet<>(10));
        }
        String methodName = "";
        if (!"".equals(fileContent)) {
            for (String line : lineList) {
                if (line.indexOf("(") == -1) {
                    continue;
                }
                if (line.indexOf("private") > 0 || line.indexOf("public") > 0 || line.indexOf("protected") > 0) {
                    for (Method method : methods) {
                        if (line.indexOf(" " + method.getName() + "(") > 0) {
                            methodName = method.getName();
                        }
                    }
                } else {
                    for (Method method : methods) {
                        if (line.indexOf(" " + method.getName() + "(") > 0) {
                            whenMethod.get(methodName).add(method.getName());
                        }
                    }
                    for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                        if (line.indexOf(entry.getKey()) > 0) {
                            whenMap.get(methodName).add(entry.getValue());
                        }
                    }
                }
            }
            for (Map.Entry<String, Set<String>> entry : whenMethod.entrySet()) {
                for (String key : entry.getValue()) {
                    whenMap.get(entry.getKey()).addAll(whenMap.get(key));
                }
            }
        }

        methods(myClass, whenMap);
        println("}");

        if (fileContent.equals("")) {
            map.forEach((key, value) -> {
                for (String item : value) {
                    println(item);
                    println("");
                }
            });
        }
    }

    private Set<Field> getDeclaredFields(Class myClass) {
        Set<Field> set = new HashSet<>(15);
        Set<Type> setType = new HashSet<>(15);
        for (Field field : myClass.getDeclaredFields()) {
            set.add(field);
            setType.add(field.getType());
        }
        if (!myClass.getSuperclass().getName().contains("java.")) {
            for (Field field : getDeclaredFields(myClass.getSuperclass())) {
                if (!setType.contains(field.getType())) {
                    set.add(field);
                    setType.add(field.getType());
                }
            }
        }
        return set;
    }

    private Set<Method> getDeclaredMethods(Class myClass, Boolean base) {
        Method[] methods = myClass.getDeclaredMethods();
        Set<Method> set = new HashSet<>(15);
        for (Method method : methods) {
            if (base || method.toString().contains("public ")) {
                set.add(method);
            }
        }

        if (myClass.getSuperclass() != null && !myClass.getSuperclass().getName().contains("java.")) {
            set.addAll(getDeclaredMethods(myClass.getSuperclass(), false));
        } else if (myClass.getInterfaces() != null && myClass.getInterfaces().length > 0) {
            set.addAll(getDeclaredMethods(myClass.getInterfaces()[0], false));
        }

        return set;
    }

    private void println(String item) {
        if (!isInit) {
            System.out.println(item);
        }
    }

    private String getWhen(Method serviceMethod, int number, String serviceName) throws Exception {
        // 生成 when thenReturn 代码
        List<String> list = new ArrayList<>(10);

        Class returnType = serviceMethod.getReturnType();
        Type genericType = serviceMethod.getGenericReturnType();
        String setLine = null;
        if (isVo(returnType)) {
            setLine = getDefType(returnType, genericType) + " then" + number  + " = get" +
                    getType(returnType.getTypeName()) + "();";
        } else {
            setLine = getDefType(returnType, genericType) + " then" + number  + " = " +
                    getDefaultVal(returnType.getTypeName()) + ";";
            if ("java.util.List".equals(returnType.getTypeName()) || "java.util.Set".equals(returnType.getTypeName())) {
                setLine = setLine + "\nthen" + number + ".add(" + getDefaultVal(genericType) + ");";
            } else if ("java.util.Map".equals(returnType.getTypeName())) {
                setLine = setLine + "\nthen" + number + ".put(" + getDefaultVal(genericType) + ");";
            }
        }
        for (Parameter param : serviceMethod.getParameters()) {
            list.add(getAny(param.getType().getName()));
        }
        return setLine + "\nwhen(" + serviceName + "." + serviceMethod.getName() + "(" + String.join(", ", list) + ")).thenReturn(then" + number + ");";
    }

    private String getDefaultVal(Type genericType) throws Exception {
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;

            Type[] types = parameterizedType.getActualTypeArguments();
            List<String> tmpList = new ArrayList<>(10);
            for (Type type : types) {
                if (type instanceof Class) {
                    Class realType = (Class) type;
                    tmpList.add(getDefaultVal(realType));

                    isVo(realType);
                } else if (type instanceof ParameterizedType) {
                    tmpList.add(getDefaultVal(type.getTypeName()));
                }
            }
            return String.join(", ", tmpList);
        }
        return "";
    }

    private String getDefaultVal(Class realType) {
        return getDefaultVal(realType.getTypeName());
    }

    private String getDefType(Class returnType, Type genericType) throws Exception {
        return getDefType(getType(returnType.getName()), genericType);
    }

    private String getDefType(String returnTypeName, Type genericType) throws Exception {
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;

            Type[] types = parameterizedType.getActualTypeArguments();
            List<String> tmpList = new ArrayList<>(10);
            for (Type type : types) {
                if (type instanceof Class) {
                    Class realType = (Class) type;
                    tmpList.add(realType.getSimpleName());

                    isVo(realType);
                } else if (type instanceof ParameterizedType) {
                    tmpList.add(getDefType(getType(type.getTypeName()), type));
                }
            }
            return returnTypeName + "<" + String.join(", ", tmpList) + ">";
        }
        return returnTypeName;
    }

    Map<String, String> defaultMap = new HashMap<>(16);
    private void methods(Class myClass, Map<String, Set<List<String>>> whenMap) throws Exception {
        Method[] publicMethod = myClass.getMethods();
        Method[] allMethod = myClass.getDeclaredMethods();
        List<Method> resultList = new ArrayList<>(publicMethod.length);
        Collections.addAll(resultList, publicMethod);

        Map<String, Integer> methodCount = new HashMap<>();
        for (Method method : allMethod) {
            if (!resultList.contains(method) && !genPrivateMethod) {
                continue;
            }

            Class[] classes = method.getParameterTypes();
            Parameter[] parameter = method.getParameters();
            Type[] genericParameterTypes = method.getGenericParameterTypes();

            List<String> meta = new ArrayList<>(classes.length);
            List<String> metaType = new ArrayList<>(classes.length);

            List<String> parameterType = getMethodParameterTypes(method);

            if (methodCount.get(method.getName()) == null) {
                methodCount.put(method.getName(), 0);
            } else {
                methodCount.put(method.getName(), methodCount.get(method.getName()) + 1);
            }

            println("");
            println("/**\n" +
                    "     * " + method.getName() + "\n" +
                    "                    *\n" +
                    "     * @throws Exception\n" +
                    "                    */");
            println("@Test");
            println("public void " + method.getName() + getMethodCountName(methodCount.get(method.getName())) + "Test() throws Exception {");
            for (int i = 0; i < classes.length; i++) {
                // 取得每个参数的初始化
                if (isVo(classes[i])) {
                    String setLine = parameterType.get(i) + " " + parameter[i].getName()  + " = get" +
                            getType(classes[i].getTypeName()) + "();";
                    println(setLine);
                } else {
                    String setLine = parameterType.get(i) + " " + parameter[i].getName()  + " = " +
                            getDefaultVal(classes[i].getTypeName()) + ";";
                    println(setLine);
                }
                meta.add(parameter[i].getName());
                metaType.add(getType(classes[i].getTypeName()) + ".class");
            }

            String returnType = method.getAnnotatedReturnType().getType().getTypeName();

            String defString = "";
            String assertString = "";
            if ("void".equals(returnType)) {
                println("String error = null;");
            } else if (returnType.indexOf("java.util.List") == 0) {
                defString = "Object result = ";
                assertString = "Assert.assertTrue(result != null && result.toString().indexOf(\"[\") == 0);";
            } else {
                if (!resultList.contains(method)) {
                    defString = "Object result = ";
                } else {
                    defString = getType(returnType) + " result = ";
                }
                assertString = "Assert.assertTrue(result != null);";
            }
            String joinStr = metaType.size() > 0 ? ", " : "";
            if (!resultList.contains(method)) {

                String superclassStr = isSuperclass ? ".getSuperclass()" : "";
                String initMethod = "Method method = " + serviceName + ".getClass()" + superclassStr + ".getDeclaredMethod(\"" + method.getName() + "\"" + joinStr
                        + String.join(", ", metaType) + ");";
                println(initMethod);
                println("method.setAccessible(true);");
                println(defString + "method.invoke(" + serviceName + joinStr + String.join(", ", meta) + ");");

            } else {
                println(defString + serviceName + "." + method.getName() + "(" + String.join(", ", meta) + ");");
            }

            Boolean add = false;
            for (int i = 0; i < classes.length; i++) {
                if ("java.util.List".equals(parameter[i].getType().getTypeName()) || "java.util.Set".equals(parameter[i].getType().getTypeName())) {
                    println(parameter[i].getName() + ".add(" + getDefaultVal(genericParameterTypes[i]) + ");");
                    add = true;
                } else if ("java.util.Map".equals(parameter[i].getType().getTypeName())) {
                    println(parameter[i].getName() + ".put(" + getDefaultVal(genericParameterTypes[i]) + ");");
                    add = true;
                }
            }
            if (add) {
                if (!resultList.contains(method)) {
                    println("method.invoke(" + serviceName + joinStr + String.join(", ", meta) + ");");
                } else {
                    println(serviceName + "." + method.getName() + "(" + String.join(", ", meta) + ");");
                }
            }

            Set<List<String>> whenList = whenMap.get(method.getName());
            if (whenList != null && !whenList.isEmpty()) {
                for (List<String> oneList : whenList) {
                    println("");
                    println(String.join("\n", oneList));
                }

                if (!resultList.contains(method)) {
                    println("method.invoke(" + serviceName + joinStr + String.join(", ", meta) + ");");
                } else {
                    println(serviceName + "." + method.getName() + "(" + String.join(", ", meta) + ");");
                }
            }

            if ("void".equals(returnType)) {
                println("try {");
                // 定义常用的 Exception
                println("} catch (" + importException.getSimpleName() + " e) {");
                println("error = e.getMessage();");
                println("}");
                println("Assert.assertTrue(error == null);");
            } else {
                println(assertString);
            }
            println("}");
        }

        for (Map.Entry<String, String> entry : defaultMap.entrySet()) {
            String localType = getType(entry.getKey());
            println("");
            String fnStr = null;
            if (useJson) {
                fnStr = "private " + localType + " get" + localType
                        + "() {\nString json = \""
                        + entry.getValue() + "\";\n" + localType + " vo = new Gson().fromJson(json, " + localType + ".class);\nreturn vo;\n}";
            } else {
                fnStr = "private " + localType + " get" + localType
                        + "() {\n" + localType + " vo = new " + localType + "();\n"
                        + entry.getValue() + "return vo;\n}";
            }
            println(fnStr);
        }
    }

    private String getMethodCountName(Integer count) {
        String[] arr = {"", "Two", "Three", "Four"};
        if (count >= 4) {
            return "";
        } else {
            return arr[count];
        }
    }

    private List<Method> getMethods(Class myClass, Class myClass2) {
        List<Method> resultList = new ArrayList<>(myClass2.getMethods().length);
        Collections.addAll(resultList, myClass2.getMethods());

        List<Method> list = new ArrayList<>(10);

        for (Method method : myClass.getMethods()) {
            if (!resultList.contains(method)) {
                list.add(method);
            }
        }
        return list;
    }

    private List<Method> getMethods(Class myClass) {
        return getMethods(myClass, Object.class);
    }

    private Boolean isVo(Class myClass) throws Exception {
        setImport(myClass.getName());
        if (myClass.getName().length() >= 5 && "java.".equals(myClass.getName().substring(0, 5))) {
            return false;
        }

        List<Method> listMethod = getMethods(myClass);
        String defaultValue = getDefaultValue(myClass.getName());
        if (!"null".equals(defaultValue)) {
            return false;
        }

        for (Method method : listMethod) {
            Class[] parameter = method.getParameterTypes();
            if (method.getName().length() > 3 && "set".equals(method.getName().substring(0, 3)) && parameter.length == 1) {
                defaultMap.put(myClass.getName(), useJson ? getAttr(myClass) : getVo(myClass));
                return true;
            }
        }
        return false;
    }

    Set<String> importSet = new HashSet<>(16);
    private void setImport(String name) {
        if (isInit && name.indexOf("[") == -1) {
            importSet.add("import " + name + ";");
        }
    }

    private String getAttr(Class myClass) throws Exception {
        List<String> result = new ArrayList<>(10);
        result.add("'" + serviceName + "':0");
        for (Method method : getMethods(myClass)) {
            Class[] parameter = method.getParameterTypes();
            if (method.getName().length() > 3 && "set".equals(method.getName().substring(0, 3)) && parameter.length == 1) {
                result.add("'" + (method.getName().substring(3, 4).toLowerCase()) + method.getName().substring(4) +
                        "':" + getDefaultValue(parameter[0].getName()));
            }
        }
        return "{" + String.join(",", result) + "}";
    }

    private String getVo(Class myClass) throws Exception {
        List<String> result = new ArrayList<>(10);
        for (Method method : getMethods(myClass)) {
            Class[] parameter = method.getParameterTypes();
            if (method.getName().length() > 3 && "set".equals(method.getName().substring(0, 3)) && parameter.length == 1 && fileContent.contains("." + method.getName() + "(")) {
                result.add("vo." + method.getName() + "(" + getDefaultVal(parameter[0].getName())  + ");\n");
            }
        }
        return String.join("", result);
    }

    private List<String> getMethodParameterTypes(Method method) throws Exception {
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        List<String> list = new ArrayList<>(genericParameterTypes.length);
        for (int i = 0; i < genericParameterTypes.length; i++) {
            Type genericType = genericParameterTypes[i];
            list.add(getDefType(getType(genericType.getTypeName()), genericType));
        }
        return list;
    }

    private String getType(String type) {
        int index = type.indexOf("<");
        String suffix = (type.indexOf("[") > 0 && index > 0) ? "[]" : "";
        if (index > 0) {
            type = type.substring(0, index);
        }
        setImport(type);
        String[] arr = type.split("[.]");
        if (arr.length > 0) {
            return arr[arr.length - 1] + suffix;
        } else {
            return "";
        }
    }

    private String getDefaultVal(String name) {
        int indexArr = name.indexOf("[");
        if (indexArr > 0) {
            name = name.substring(0, indexArr);
        }
        int index = name.indexOf("<");
        if (index > 0) {
            name = name.substring(0, index);
        }
        String result = null;
        switch (name) {
            case "java.math.BigDecimal":
                result = "new BigDecimal(1)";
                break;
            case "java.math.BigInteger":
                result = "new BigInteger(1)";
                break;
            case "short":
                result = "(short)0";
                break;
            case "byte":
                result = "(byte)1";
                break;
            case "char":
                result = "'1'";
                break;
            case "long":
            case "java.lang.Long":
                result = "1L";
                break;
            case "int":
            case "java.lang.Integer":
                result = "1";
                break;
            case "double":
            case "java.lang.Double":
                result = "1.0D";
                break;
            case "float":
            case "java.lang.Float":
                result = "1.0F";
                break;
            case "java.lang.String":
                result = "\"1\"";
                break;
            case "java.lang.Boolean":
            case "boolean":
                result = "true";
                break;
            case "java.util.List":
                setImport("java.util.ArrayList");
                result = "new ArrayList<>(10)";
                break;
            case "java.util.Map":
                setImport("java.util.HashMap");
                result = "new HashMap<>(16)";
                break;
            case "java.util.Set":
                setImport("java.util.HashSet");
                result = "new HashSet<>(16)";
                break;
            default:
                if (defaultMap.get(name) != null) {
                    result = "get" + getType(name) + "()";
                } else {
                    result = "new " + getType(name) + "()";
                }
        }
        if (indexArr > 0) {
            switch (name) {
                case "java.util.List":
                    result = "new ArrayList[1]";
                    break;
                case "java.util.Map":
                    result = "new HashMap[1]";
                    break;
                case "java.util.Set":
                    result = "new HashSet[1]";
                    break;
                default:
                    result = "{" + result + "}";
            }
            return result;
        }
        return result;
    }

    private String getDefaultValue(String name) {
        String result;
        switch (name) {
            case "java.math.BigDecimal":
            case "java.math.BigInteger":
            case "long":
            case "int":
            case "short":
            case "double":
            case "float":
            case "byte":
            case "char":
            case "java.lang.Long":
            case "java.lang.Integer":
            case "java.lang.Short":
            case "java.lang.Double":
            case "java.lang.Float":
            case "java.lang.String":
                result = "'1'";
                break;
            case "java.lang.Boolean":
            case "boolean":
                result = "true";
                break;
            case "java.util.List":
            case "java.util.Set":
                result = "[]";
                break;
            case "java.util.Map":
                result = "{}";
                break;
            case "java.util.Date":
                // gson 需要自定义 SimpleDateFormat result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                result = "null";
                break;
            default:
                result = "null";
        }
        return result;
    }

    private String getAny(String name) {
        String result = null;
        switch (name) {
            case "short":
                setImport(importAny + ".anyShort");
                result = "anyShort()";
                break;
            case "byte":
                setImport(importAny + ".anyByte");
                result = "anyByte()";
                break;
            case "char":
                setImport(importAny + ".anyChar");
                result = "anyChar()";
                break;
            case "long":
            case "java.lang.Long":
                setImport(importAny + ".anyLong");
                result = "anyLong()";
                break;
            case "int":
            case "java.lang.Integer":
                setImport(importAny + ".anyInt");
                result = "anyInt()";
                break;
            case "double":
            case "java.lang.Double":
                setImport(importAny + ".anyDouble");
                result = "anyDouble()";
                break;
            case "float":
            case "java.lang.Float":
                setImport(importAny + ".anyFloat");
                result = "anyFloat()";
                break;
            case "java.lang.String":
                setImport(importAny + ".anyString");
                result = "anyString()";
                break;
            case "java.lang.Boolean":
            case "boolean":
                setImport(importAny + ".anyBoolean");
                result = "anyBoolean()";
                break;
            case "java.util.List":
                setImport(importAny + ".anyList");
                result = "anyList()";
                break;
            case "java.util.Map":
                setImport(importAny + ".anyMap");
                result = "anyMap()";
                break;
            case "java.util.Set":
                setImport(importAny + ".anySet");
                result = "anySet()";
                break;
            case "java.util.Date":
            default:
                setImport(importAny + ".any");
                result = "any()";
        }
        return result;
    }

    private static List<String> readFileContent(Class myClass) {
        String fileName = getAbsolutePath(myClass);
        List<String> sbf = new ArrayList<>(10);
        if (ObjectUtils.isEmpty(fileName)) {
            return sbf;
        }
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.add(tempStr);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        if (!myClass.getSuperclass().getName().contains("java.")) {
            sbf.addAll(readFileContent(myClass.getSuperclass()));
        }
        return sbf;
    }
}