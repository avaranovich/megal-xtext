/*
 * generated by Xtext
 */
package org.softlang.megal.language.validation;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.validation.ComposedChecks;

@ComposedChecks(validators= {org.softlang.megal.language.validation.IntegrityValidation.class, org.softlang.megal.language.validation.ModelExecutingValidation.class})
public class AbstractMegalValidator extends org.eclipse.xtext.validation.AbstractDeclarativeValidator {

	@Override
	protected List<EPackage> getEPackages() {
	    List<EPackage> result = new ArrayList<EPackage>();
	    result.add(EPackage.Registry.INSTANCE.getEPackage("http://softlang.wikidot.com/megal"));
		return result;
	}
}