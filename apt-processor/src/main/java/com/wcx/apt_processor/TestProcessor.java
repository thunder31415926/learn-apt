package com.wcx.apt_processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.wcx.apt_lib.Test;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class) // 编译期绑定
//@SupportedAnnotationTypes({"com.wcx.apt_lib.Test"}) // 表示我要处理那个注解
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class TestProcessor extends AbstractProcessor {

    // 编译期打印日志
    private Messager messager;

    // Java文件生成器
    private Filer filer;

    /**
     * 这个方法做一些初始化工作，方法中有一个ProcessingEnvironment类型的参数，ProcessingEnvironment是一个注解处理工具的集合。它包含了众多工具类。例如：
     * Filer        可以用来编写新文件
     * Messager     可以用来打印错误信息
     * Elements     是一个可以处理Element的工具类
     * @param processingEnv
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
    }

    /**
     * 这个方法非常简单，只有一个返回值，用来指定当前正在使用的Java版本，通常return SourceVersion.latestSupported()即可。
     * 和TestProcessor上@SupportedSourceVersion注解一样效果
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        //return SourceVersion.latestSupported();
        return SourceVersion.RELEASE_8;
    }

    /**
     * 这个方法的返回值是一个Set集合，集合中指要处理的注解类型的名称(这里必须是完整的包名+类名，例如com.wcx.apt_lib.Test)。由于在本例中只需要处理@Test，因此Set集合中只需要添加@Test。
     * 和TestProcessor上@SupportedAnnotationTypes注解一样效果
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //HashSet<String> supportTypes = new LinkedHashSet<>();
        //supportTypes.add(BindView.class.getCanonicalName());
        //return supportTypes;
        return Collections.singleton(Test.class.getCanonicalName());
    }

    /**
     * 在这个方法的方法体中，我们可以校验被注解的对象是否合法、可以编写处理注解的代码，以及自动生成需要的java文件等。因此说这个方法是AbstractProcessor 中的最重要的一个方法。我们要处理的大部分逻辑都是在这个方法中完成。
     * 这个方法的返回值，是一个boolean类型，返回值表示注解是否由当前Processor处理
     * true     :则这些注解由此注解来处理，后续其它的 Processor 无需再处理它们；
     * false    :则这些注解未在此Processor中处理并，那么后续 Processor 可以继续处理它们。
     * @param annotations
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(annotations.isEmpty()){
            messager.printMessage(Diagnostic.Kind.NOTE, "没有发现被ARouter注解的类");
            return false; // 标注注解处理器没有工作
        }
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "我就是测试一下编码啦~~~~");
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();
        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();
        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
                .build();
        try {
            javaFile.writeTo(filer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
