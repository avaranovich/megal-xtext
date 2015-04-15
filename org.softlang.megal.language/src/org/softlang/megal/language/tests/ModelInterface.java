package org.softlang.megal.language.tests;

import java.io.File;
import java.io.IOException;

import org.softlang.megal.Megamodel;
import org.softlang.megal.language.Megals;
import org.softlang.megal.mi2.Entity;
import org.softlang.megal.mi2.EntityType;
import org.softlang.megal.mi2.KB;
import org.softlang.megal.mi2.KBs;
import org.softlang.megal.mi2.MegamodelKB;
import org.softlang.megal.mi2.NaiveReasoner;
import org.softlang.megal.mi2.Relationship;
import org.softlang.megal.mi2.RelationshipType;
import org.softlang.megal.mi2.instances.MakePartOfs;

public class ModelInterface {
	private static String PRELUDE = "./src/org/softlang/megal/language/tests/Prelude.megal";

	private static String AS = "./src/org/softlang/megal/language/tests/As.megal";

	public static void main(String[] args) throws IOException {

		Megamodel mm = load();

		MegamodelKB kb = new MegamodelKB(mm);

		NaiveReasoner mi = new NaiveReasoner(kb);

		dump(mi);

		testInOut(mi);

		testGCD(mi);

		testFindEvaluators(mi);

		testSubtypes(mi);

		testAllSubtypes(mi);

		testInstances(mi);

		testComputations(mi);
	}

	private static Megamodel load() throws IOException {
		System.out.print("Loading ...");
		Megamodel mm = Megals.load(new File(AS), new File(PRELUDE));
		System.out.println(" done.");
		return mm;
	}

	private static void dump(NaiveReasoner mi) {
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
		for (Entity x : mi.getEntities())
			System.out.println("  " + x);
		System.out.println();

		System.out.println("Relationships");
		for (Relationship x : mi.getRelationships())
			System.out.println("  " + x);
		System.out.println();
	}

	private static void testInOut(NaiveReasoner mi) {
		System.out.println("TEST IN OUT");

		Entity entity = mi.getEntity("OnlyAs");
		for (Relationship part : entity.incoming())
			System.out.println("->: " + part);
		for (Relationship part : entity.outgoing())
			System.out.println("<-: " + part);
		System.out.println();
	}

	private static void testGCD(NaiveReasoner mi) {
		System.out.println("TEST GCD");

		EntityType a = mi.getEntityType("Folder");
		EntityType b = mi.getEntityType("Fragment");
		EntityType c = mi.getEntityType("Language");

		EntityType x = a.gcd(b);
		EntityType y = a.gcd(c);
		EntityType z = a.gcd(a);
		System.out.println("gcd(Folder, Fragment)=" + x.getName());
		System.out.println("gcd(Folder, Language)=" + y.getName());
		System.out.println("gcd(Folder, Folder)=" + z.getName());
		System.out.println();
	}

	private static void testFindEvaluators(NaiveReasoner mi) {
		System.out.println("TEST FIND EVALUATORS");

		EntityType ev = mi.getEntityType("Evaluator");
		System.out.println("Evaluators:");
		for (Entity e : ev.getInstances())
			System.out.println("  " + e);
		System.out.println();
	}

	private static void testSubtypes(NaiveReasoner mi) {
		System.out.println("TEST SUBTYPES");

		EntityType a = mi.getEntityType("Artifact");

		System.out.println("Subtypes of artifact:");
		for (EntityType e : a.getSubtypes())
			System.out.println("  " + e);
		System.out.println();
	}

	private static void testAllSubtypes(NaiveReasoner mi) {
		System.out.println("TEST ALL SUBTYPES");

		EntityType a = mi.getEntityType("Artifact");

		System.out.println("All subtypes of artifact:");
		for (EntityType e : a.getAllSubtypes())
			System.out.println("  " + e);
		System.out.println();
	}

	private static void testInstances(NaiveReasoner mi) {
		System.out.println("TEST INSTANCES");

		EntityType a = mi.getEntityType("File");
		EntityType b = mi.getEntityType("File");

		System.out.println("Instances of file:");
		for (Entity e : a.getInstances())
			System.out.println("  " + e);

		System.out.println("All instances of file:");
		for (Entity e : b.getAllInstances())
			System.out.println("  " + e);
		System.out.println();
	}

	private static void testComputations(NaiveReasoner mi) {
		System.out.println("TEST COMPUTATIONS");

		KB zwei = MakePartOfs.makePartOfs(mi);
		KB drei = KBs.union(mi.getKB(), zwei);
		NaiveReasoner reasonerDrei = new NaiveReasoner(drei);

		for (Relationship r : reasonerDrei.getRelationships()) {
			if ("partOf".equals(r.getType().getName())) {
				System.out.println("Encountered part of");
				System.out.println("  " + r.getLeft().getType());
				System.out.println("  " + r.getRight().getType());
			}
		}
		dump(reasonerDrei);

		System.out.println();
	}
}
