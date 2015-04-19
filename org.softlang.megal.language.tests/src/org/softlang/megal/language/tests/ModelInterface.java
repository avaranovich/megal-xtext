package org.softlang.megal.language.tests;

import java.io.File;
import java.io.IOException;

import org.softlang.megal.Megamodel;
import org.softlang.megal.api.Evaluators;
import org.softlang.megal.api.Result;
import org.softlang.megal.language.Megals;
import org.softlang.megal.mi2.Entity;
import org.softlang.megal.mi2.EntityType;
import org.softlang.megal.mi2.KB;
import org.softlang.megal.mi2.MegamodelKB;
import org.softlang.megal.mi2.Relationship;
import org.softlang.megal.mi2.RelationshipType;
import org.softlang.megal.mi2.processing.PartOfProcessor;
import org.softlang.megal.mi2.processing.Processor;
import org.softlang.megal.mi2.processing.ResolutionProcessor;
import org.softlang.megal.mi2.processing.UnionProcessor;
import org.softlang.megal.mi2.reasoning.Providers;
import org.softlang.megal.mi2.reasoning.Reasoner;
import org.softlang.sourcesupport.LocalSourceSupport;

public class ModelInterface {
	private static String PRELUDE = "./src/org/softlang/megal/language/tests/Prelude.megal";

	private static String AS = "./src/org/softlang/megal/language/tests/As.megal";

	public static void main(String[] args) throws IOException {
		// Make the processor chain of first partOf materialization and then
		// application of resolvers
		Processor processChain = UnionProcessor.of(PartOfProcessor.INSTANCE, ResolutionProcessor.INSTANCE);

		Megamodel mm = load();
		KB a = MegamodelKB.loadAll(mm);
		KB b = processChain.applyWith(a);

		dump(b);

		Result result = Evaluators.evaluate(LocalSourceSupport.INSTANCE, Providers.obtain(b));
		System.out.println(result);
	}

	private static Megamodel load() throws IOException {
		System.out.print("Loading ...");
		Megamodel mm = Megals.load(new File(AS), new File(PRELUDE));
		System.out.println(" done.");
		return mm;
	}

	private static void dump(KB kb) {
		dump(Providers.obtain(kb));
	}

	private static void dump(Reasoner mi) {
		System.out.println("Megamodel " + mi.getTitle());

		System.out.println("Entity types");
		for (EntityType x : mi.getEntityTypes())
			System.out.println("  " + x);
		System.out.println();

		System.out.println("Relationship types");
		for (RelationshipType x : mi.getRelationshipTypes())
			System.out.println("  " + x);
		System.out.println();

		System.out.println("Entities");
		for (Entity x : mi.getEntities()) {
			System.out.println("  " + x);
			for (String s : x.getBindings())
				System.out.println("  ~ " + s);
		}
		System.out.println();

		System.out.println("Relationships");
		for (Relationship x : mi.getRelationships())
			System.out.println("  " + x);
		System.out.println();
	}
}
