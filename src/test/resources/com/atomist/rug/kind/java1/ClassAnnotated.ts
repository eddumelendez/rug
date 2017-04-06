// Generated by Rug to TypeScript transpiler.

import { EditProject } from '@atomist/rug/operations/ProjectEditor'
import { PathExpressionEngine } from '@atomist/rug/tree/PathExpression'
import { Editor, Tags, Parameter } from '@atomist/rug/operations/Decorators'
import { Pattern } from '@atomist/rug/operations/RugOperation'
import { JavaSource, JavaType, Project } from '@atomist/rug/model/Core'

/**
    ClassAnnotated
    I add FooBar annotation
 */
@Editor("ClassAnnotated", "I add FooBar annotation")
class ClassAnnotated implements EditProject {

    edit(project: Project) {
        let eng: PathExpressionEngine = project.context.pathExpressionEngine;
        eng.with<JavaSource>(project, '//JavaSource()', j => {
            eng.with<JavaType>(j, '//JavaType()', c => {
                c.addAnnotation("com.foo", "FooBar")
            })
        })
    }
}
export let editor_classAnnotated = new ClassAnnotated();