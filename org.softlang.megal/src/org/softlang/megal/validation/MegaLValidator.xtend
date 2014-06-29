/*
 * generated by Xtext
 */
package org.softlang.megal.validation

import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.validation.Check
import org.softlang.megal.megaL.ED
import org.softlang.megal.megaL.ETD
import org.softlang.megal.megaL.MegaLDefinition
import org.softlang.megal.megaL.MegaLLinking
import org.softlang.megal.megaL.MegaLPackage
import org.softlang.megal.megaL.RD
import org.softlang.megal.megaL.RTD
import org.softlang.megal.semantics.Diagnostic

import static extension org.softlang.megal.attachment.Attachment.*
import static extension org.softlang.megal.calculation.Calculation.*
import org.softlang.megal.semantics.SemanticsRegistry
import com.google.common.base.Optional
import org.softlang.megal.megaL.Jar

/**
 * Custom validation rules. 
 *
 * see http://www.eclipse.org/Xtext/documentation.html#validation
 */
class MegaLValidator extends AbstractMegaLValidator {
	private static class DiagnosticWrapper implements Diagnostic {
		val MegaLValidator validator
		val EObject object
		val EStructuralFeature feature

		new(MegaLValidator validator, EObject object, EStructuralFeature feature) {
			this.validator = validator
			this.object = object
			this.feature = feature
		}

		override info(String string) {
			validator.acceptInfo(string, object, feature, -1, null)
		}

		override warning(String string) {
			validator.acceptWarning(string, object, feature, -1, null)
		}

		override error(String string) {
			validator.acceptError(string, object, feature, -1, null)
		}

	}

	@Check
	def checkSemanticsExisting(ETD e) {
		val c = e.eResource.getContextOrCreate[|SemanticsRegistry.INSTANCE.contextInstance]

		val s = c.getSoftEntitySemantics(e)
		val h = c.getHardEntitySemantics(e)

		if (s != null && h == null)
			info('Soft implementation for ' + e.name, MegaLPackage.Literals.NAMED_DEFINITION__NAME)
		if (s == null && h == null)
			error('No implementation for ' + e.name, MegaLPackage.Literals.NAMED_DEFINITION__NAME)
	}

	@Check
	def checkSemanticsExisting(RTD r) {
		val c = r.eResource.getContextOrCreate[|SemanticsRegistry.INSTANCE.contextInstance]
		val s = c.getSoftRelationSemantics(r)
		val h = c.getHardRelationSemantics(r)

		if (s != null && h == null)
			info('Soft implementation for ' + r.name, MegaLPackage.Literals.NAMED_DEFINITION__NAME)
		if (s == null && h == null)
			error('No implementation for ' + r.name, MegaLPackage.Literals.NAMED_DEFINITION__NAME)

	}

	@Check
	def checkIsLinked(MegaLDefinition it) {
		val relative = eResource.URI.deresolve(URI.createURI("platform:/resource/"))
		val project = relative.segment(0)

		val folder = ResourcesPlugin.workspace.root.getProject(project).getFolder("plugins");

		if (folder.exists)
			if (linker == null)
				warning('Model is not linked', MegaLPackage.Literals.MODEL__NAME)
	}

	@Check
	def checkIsLinked(MegaLLinking it) {
		if (target == null)
			warning('Linking is not targeting a model', MegaLPackage.Literals.MODEL__NAME)
	}

	@Check
	def checkIsLinked(ED e) {
		val m = e.eContainer as MegaLDefinition

		if (!m.linker.lds.exists[l|EcoreUtil.equals(l.target, e)])
			warning('Unlinked entity', MegaLPackage.Literals.NAMED_DEFINITION__NAME)
	}

	@Check
	def checkEntity(ED e) {
		val c = e.eResource.getContextOrCreate[|SemanticsRegistry.INSTANCE.contextInstance]

		val d = new MegaLValidator.DiagnosticWrapper(this, e, MegaLPackage.Literals.NAMED_DEFINITION__NAME)

		val m = e.eContainer as MegaLDefinition
		val s = c.hardEntitySemantics.get(e.type.name)
		val l = Optional.fromNullable(m.linker.lds.findFirst[l|EcoreUtil.equals(l.target, e)])

		if (s != null)
			s.validate(d, e, l)
	}

	@Check
	def checkRelation(RD r) {
		val c = r.eResource.getContextOrCreate[|SemanticsRegistry.INSTANCE.contextInstance]

		val d = new MegaLValidator.DiagnosticWrapper(this, r, MegaLPackage.Literals.RD__REL)

		val m = r.eContainer as MegaLDefinition
		val s = c.hardRelationSemantics.get(r.rel.name)
		val ls = Optional.fromNullable(m.linker.lds.findFirst[l|EcoreUtil.equals(l.target, r.source)])
		val lt = Optional.fromNullable(m.linker.lds.findFirst[l|EcoreUtil.equals(l.target, r.target)])

		if (s != null)
			s.validate(d, r, ls, lt)
	}

	@Check
	def checkRelationApplicable(RD r) {
		val a = r.appliedRTD

		if (a.present)
			info('''Applied overload from «a.get.domain.name» to «a.get.coDomain.name»''',
				MegaLPackage.Literals.RD__REL)
		else
			error('''There is no applicable overload matching «r.source.type.name» to «r.target.type.name» ''',
				MegaLPackage.Literals.RD__REL)
	}

	@Check
	def checkJarUnique(Jar j) {
		val l = j.eContainer as MegaLLinking
		val i = l.jars.indexOf(j)

		if (l.jars.filter[ref == j.ref].exists[l.jars.indexOf(it) < i])
			warning('''Already imported this jar''', MegaLPackage.Literals.JAR__REF)
	}

}
