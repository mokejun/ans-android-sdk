package com.analysys.plugin.allgro

import com.analysys.plugin.allgro.asm.visitor.AnalysysClassVisitor
import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.objectweb.asm.ClassVisitor

abstract class AnalysysAsmClassVisitorFactory implements AsmClassVisitorFactory<InstrumentationParameters.None> {

    @Override
    ClassVisitor createClassVisitor(ClassContext classContext, ClassVisitor nextClassVisitor) {
        String className = toCheckerClassName(classContext.getCurrentClassData().getClassName())
        ClassChecker checker = ClassChecker.create(className)
        return new AnalysysClassVisitor(nextClassVisitor, checker)
    }

    @Override
    boolean isInstrumentable(ClassData classData) {
        String className = toCheckerClassName(classData.getClassName())
        return ClassChecker.create(className).isShouldModify()
    }

    private static String toCheckerClassName(String internalName) {
        return internalName.replace('/', '.') + '.class'
    }
}
