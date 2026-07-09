package com.analysys.plugin

import com.analysys.plugin.allgro.AnalysysAsmClassVisitorFactory
import com.analysys.plugin.allgro.AnalysysExtension
import com.analysys.plugin.allgro.ClassChecker
import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class AnalysysPlugin implements Plugin<Project> {

    void apply(Project project) {
        final def log = project.logger
        log.error "========================"
        log.error "欢迎使用易观方舟自动埋点插件!"
        log.error "========================"

        if (!project.plugins.hasPlugin(AppPlugin)) {
            return
        }

        AnalysysExtension extension = project.extensions.create("analysysConfig", AnalysysExtension)

        project.afterEvaluate {
            ClassChecker.setExtension(extension)
        }

        def androidComponents = project.extensions.getByType(AndroidComponentsExtension)
        androidComponents.onVariants(androidComponents.selector().all()) { variant ->
            variant.instrumentation.transformClassesWith(
                    AnalysysAsmClassVisitorFactory.class,
                    InstrumentationScope.ALL
            ) { }
            variant.instrumentation.setAsmFramesComputationMode(
                    FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
            )
        }
    }
}
