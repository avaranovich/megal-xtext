/*
 * generated by Xtext
 */
package org.softlang.megal.language.generator

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator
import org.softlang.megal.Megamodel

/**
 * Generates code from your model files on save.
 * 
 * see http://www.eclipse.org/Xtext/documentation.html#TutorialCodeGeneration
 */
class MegalGenerator implements IGenerator {
	override void doGenerate(Resource resource, IFileSystemAccess fsa) {
		for (m : resource.contents.filter(Megamodel)) {
			//			val dxb = new ByteArrayOutputStream
			//			val dxp = new PrintStream(dxb)
			//			try {
			//				val psk = Evaluators.evaluateParallel(ForkJoinPool.commonPool, m)
			//
			//				//			for (e : m.declarations.filter(Entity))
			//				//				for (r : resolvers.values.filter[resolves(e)])
			//				//					println('''«r» resolves «e», value: «r.resolve(e)»''')
			//				dxp.println(psk.join)
			//				fsa.generateFile('''«m.name».report.txt''', '''«dxb.toString»''')
			//			} catch (Exception e) {
			//				e.printStackTrace
			//			}
		}

	//		if (resource.contents.filter(Megamodel).exists[info.exists[key == "Generated"]])
	//			return
	//
	//		val funAppDesugaring = new FunAppDesugaring
	//		val languageResolving = new LanguageResolving
	//
	//		for (m : resource.contents.filter(Megamodel)) {
	//			if (!m.info.exists[key == "Generated"])
	//				m.info += createAnnotation => [key = "Generated"]
	//				
	//			m.name = m.name + ".Processed"
	//			funAppDesugaring.apply(m)
	//			languageResolving.apply(m)
	//		}
	//
	//		val baos = new ByteArrayOutputStream
	//		resource.save(baos, emptyMap)
	//		val result = new String(baos.toByteArray)
	//		fsa.generateFile('''«resource.URI.trimFileExtension.lastSegment».Processed.megal''', result)
	}
}
