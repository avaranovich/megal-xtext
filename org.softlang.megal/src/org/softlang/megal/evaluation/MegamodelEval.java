package org.softlang.megal.evaluation;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.softlang.megal.Annotation;
import org.softlang.megal.Declaration;
import org.softlang.megal.EcoreCollector;
import org.softlang.megal.Element;
import org.softlang.megal.Elements;
import org.softlang.megal.Entity;
import org.softlang.megal.EntityType;
import org.softlang.megal.EntityTypeReference;
import org.softlang.megal.Graph;
import org.softlang.megal.Link;
import org.softlang.megal.Megamodel;
import org.softlang.megal.Relationship;
import org.softlang.megal.RelationshipType;
import org.softlang.megal.impl.MegamodelImpl;

import com.google.common.base.Objects;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

public class MegamodelEval extends MegamodelImpl {
	@Override
	public Megamodel getMegamodel() {
		return this;
	}

	@Override
	public boolean isAssigned() {
		return getName() != null;
	}

	@Override
	public Set<Link> getVisibleBindings() {
		// Extend by imports and then collect all bindings
		return Graph.<Megamodel> extendBy(this, m -> m.getImports()).stream()
				.flatMap(m -> m.getBindings().stream())
				.collect(Collectors.toSet());
	}

	@Override
	public Set<Declaration> getVisibleDeclarations() {

		// Extend by imports and then collect all declarations
		return Graph.<Megamodel> extendBy(this, m -> m.getImports()).stream()
				.flatMap(m -> m.getDeclarations().stream())
				.collect(Collectors.toSet());
	}

	@Override
	public EList<EntityType> alternativeEntityTypes(Entity entity) {
		Stream<EntityType> all = getVisibleDeclarations().stream()
				.filter(k -> k instanceof EntityType).map(k -> (EntityType) k);

		if (entity == null)
			return all.collect(EcoreCollector.toEList());

		// TODO: Entity type reference contains many and parameters, not
		// included yet

		// Get where entity is target
		Set<EntityType> ins = FluentIterable.from(getVisibleDeclarations())
				.filter(Relationship.class)
				.filter(k -> k.getRight().equalBase(entity))
				.transformAndConcat(k -> k.getType().getInstances())
				.transform(k -> k.getRight().getDefinition()).toSet();

		// Get where entity is source

		Set<EntityType> outs = FluentIterable.from(getVisibleDeclarations())
				.filter(Relationship.class)
				.filter(k -> k.getLeft().equalBase(entity))
				.transformAndConcat(k -> k.getType().getInstances())
				.transform(k -> k.getLeft().getDefinition()).toSet();

		// // Add all subtypes in the declarations
		// Consumer<Set<EntityType>> extendToAllSubtypes = s -> {
		// boolean rerun;
		// do {
		// List<EntityType> add = Lists.newArrayList();
		// for (EntityType k : s)
		// for (Declaration d : getVisibleDeclarations())
		// if (d instanceof EntityType) {
		// EntityType l = (EntityType) d;
		// if (l.getSupertype() != null
		// && l.getSupertype().getDefinition() == k)
		// add.add(l);
		// }
		//
		// rerun = s.addAll(add);
		//
		// } while (rerun);
		// };
		//
		// extendToAllSubtypes.accept(ins);
		// extendToAllSubtypes.accept(outs);

		// If either in or out is not empty, check for containment of type
		return all.filter(
				k -> (ins.isEmpty() || ins.contains(k))
						&& (outs.isEmpty() || outs.contains(k))).collect(
				EcoreCollector.toEList());
	}

	@Override
	public EList<RelationshipType> applicableRelationshipTypes(Entity left,
			Entity right) {

		// Decide on the four cases of input
		if (left == null) {
			if (right == null)
				return getVisibleDeclarations().stream()
				// Filter only to relationship types
						.filter(k -> k instanceof RelationshipType)
						// Cast them all
						.map(k -> (RelationshipType) k)
						// Collect to EList
						.collect(EcoreCollector.toEList());
			else
				return getVisibleDeclarations().stream()
						// Filter relationship types
						.filter(k -> k instanceof RelationshipType)
						// Cast them all
						.map(k -> (RelationshipType) k)
						// Filter right assignable
						.filter(k -> k.getRight() != null
								&& k.getRight().getDefinition() != null
								&& k.getRight().isAssignableFrom(
										right.getType()))
						// Collect to EList
						.collect(EcoreCollector.toEList());

		} else {
			if (right == null)
				return getVisibleDeclarations().stream()
						// Filter relationship types
						.filter(k -> k instanceof RelationshipType)
						// Cast them all
						.map(k -> (RelationshipType) k)
						// Filter left assignable
						.filter(k -> k.getLeft() != null
								&& k.getLeft().getDefinition() != null
								&& k.getLeft().isAssignableFrom(left.getType()))
						// Collect to EList
						.collect(EcoreCollector.toEList());
			else
				return getVisibleDeclarations().stream()
						// Filter relationship types
						.filter(k -> k instanceof RelationshipType)
						// Cast them all
						.map(k -> (RelationshipType) k)
						// Filter right assignable
						.filter(k -> k.getRight() != null
								&& k.getRight().getDefinition() != null
								&& k.getRight().isAssignableFrom(
										right.getType()))
						// Filter left assignable
						.filter(k -> k.getLeft() != null
								&& k.getLeft().getDefinition() != null
								&& k.getLeft().isAssignableFrom(left.getType()))
						// Collect to EList
						.collect(EcoreCollector.toEList());
		}
	}

	@Override
	public EList<Annotation> fetchInfos(Element element) {
		// Get all equal elements and flat map to their infos
		return Stream
				.concat(getVisibleBindings().stream(),
						getVisibleDeclarations().stream())
				.filter(k -> EcoreUtil.equals(k, element))
				.flatMap(k -> k.getInfo().stream())
				.collect(EcoreCollector.toEList());
	}

	@Override
	public boolean equalBase(Element other) {
		if (!(other instanceof Megamodel))
			return false;

		Megamodel mother = (Megamodel) other;

		if (!Elements.equalBase(getBindings(), mother.getBindings()))
			return false;

		if (!Elements.equalBase(getDeclarations(), mother.getDeclarations()))
			return false;

		if (!EcoreUtil.equals(getImports(), mother.getImports()))
			return false;

		if (!Objects.equal(getName(), mother.getName()))
			return false;

		return true;
	}
}
