package net.kr9ly.entitysupports;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import javax.annotation.Generated;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Copyright 2015 kr9ly
 * <br />
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <br />
 * http://www.apache.org/licenses/LICENSE-2.0
 * <br />
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class SpecHelper {

    public static AnnotationSpec getGeneratedAnnotation() {
        return AnnotationSpec.builder(Generated.class)
                .addMember("value", "$S", "net.kr9ly.entitysupports.EntitySupportsProcessor")
                .build();
    }

    public static String getTypeName(ProcessingEnvironment processingEnv, TypeMirror typeMirror) {
        return processingEnv.getTypeUtils().asElement(typeMirror).getSimpleName().toString();
    }

    public static String getPackageName(ProcessingEnvironment processingEnv, Element element) {
        return processingEnv.getElementUtils().getPackageOf(element).getQualifiedName().toString();
    }

    public static boolean isEnumType(ProcessingEnvironment processingEnv, TypeMirror type) {
        if (processingEnv.getTypeUtils().asElement(type) != null &&  ((TypeElement) processingEnv.getTypeUtils().asElement(type)).getSuperclass() != null) {
            TypeMirror enumType = processingEnv.getElementUtils().getTypeElement(Enum.class.getCanonicalName()).asType();
            TypeMirror superclass = ((TypeElement) processingEnv.getTypeUtils().asElement(type)).getSuperclass();
            return processingEnv.getTypeUtils().isSameType(superclass, processingEnv.getTypeUtils().getDeclaredType((TypeElement) processingEnv.getTypeUtils().asElement(enumType), type));
        }
        return false;
    }

    public static boolean isString(ProcessingEnvironment processingEnv, TypeMirror type) {
        return processingEnv.getTypeUtils().isSameType(type, processingEnv.getElementUtils().getTypeElement(String.class.getCanonicalName()).asType());
    }

    public static AnnotationValue getAnnotationValue(Element elem, Class<? extends Annotation> action) {
        String actionName = action.getName();
        for (AnnotationMirror am : elem.getAnnotationMirrors()) {
            if (actionName.equals(
                    am.getAnnotationType().toString())) {
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().entrySet()) {
                    if ("value".equals(
                            entry.getKey().getSimpleName().toString())) {
                        return entry.getValue();
                    }
                }
            }
        }
        return null;
    }
}
