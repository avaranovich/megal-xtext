package org.softlang.megal.mi2;

import static com.google.common.base.Objects.equal;
import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Iterables.elementsEqual;
import static com.google.common.collect.Lists.reverse;
import static com.google.common.collect.Lists.transform;
import static com.google.common.collect.Maps.immutableEntry;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Multimaps.index;
import static com.google.common.collect.Multimaps.transformValues;
import static com.google.common.collect.Tables.immutableCell;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.softlang.megal.MegalAnnotation;
import org.softlang.megal.MegalDeclaration;
import org.softlang.megal.MegalElement;
import org.softlang.megal.MegalEntity;
import org.softlang.megal.MegalEntityType;
import org.softlang.megal.MegalFile;
import org.softlang.megal.MegalFunctionApplication;
import org.softlang.megal.MegalLink;
import org.softlang.megal.MegalRelationship;
import org.softlang.megal.MegalRelationshipType;
import org.softlang.megal.mi2.util.HashMultitable;
import org.softlang.megal.mi2.util.Multitable;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Table.Cell;

/**
 * <p>
 * Wrapper for megamodels in MegaL notation.
 * </p>
 * 
 * @author Pazuzu
 *
 */
public class MegamodelKB extends KB {
	/**
	 * <p>
	 * Annotation key of the generics of an entity.
	 * </p>
	 */
	public static final String PARAMS = "#params";

	/**
	 * <p>
	 * Annotation key of the left generics of a relationship type.
	 * </p>
	 */
	public static final String PARAMS_LEFT = "#paramsLeft";

	/**
	 * <p>
	 * Annotation key of the right generics of a relationship type.
	 * </p>
	 */
	public static final String PARAMS_RIGHT = "#paramsRight";

	/**
	 * <p>
	 * Relationship name for the element of relationship.
	 * </p>
	 */
	public static final String ELEMENT_OF = "elementOf";

	/**
	 * <p>
	 * Relationship name for the first of relationship.
	 * </p>
	 */
	public static final String FIRST_OF = "firstOf";

	/**
	 * <p>
	 * Relationship name for the second of relationship.
	 * </p>
	 */
	public static final String SECOND_OF = "secondOf";

	/**
	 * <p>
	 * Entity type pair.
	 * </p>
	 */
	public static final String PAIR = "Pair";

	/**
	 * <p>
	 * Resolves to the corresponding entity type in the MegaL notation.
	 * </p>
	 * 
	 * @param megamodel
	 *            The megamodel in MegaL notation
	 * @param entityType
	 *            The item to resolve
	 * @return The resolved item
	 */
	public static MegalEntityType resolve(boolean transitive, MegalFile megamodel, EntityType entityType) {
		for (MegalEntityType item : from(megamodel.getDeclarations()).filter(MegalEntityType.class))
			if (equal(item.getName(), entityType.getName()))
				return item;

		if (transitive)
			for (MegalFile imported : reverse(megamodel.getImports())) {
				MegalEntityType potential = resolve(transitive, imported, entityType);
				if (potential != null)
					return potential;
			}

		return null;
	}

	/**
	 * <p>
	 * Resolves to the corresponding entity in the MegaL notation.
	 * </p>
	 * 
	 * @param megamodel
	 *            The megamodel in MegaL notation
	 * @param entity
	 *            The item to resolve
	 * @return The resolved item
	 */
	public static MegalEntity resolve(boolean transitive, MegalFile megamodel, Entity entity) {
		for (MegalEntity item : from(megamodel.getDeclarations()).filter(MegalEntity.class)) {
			// Not equal name, no match
			if (!equal(item.getName(), entity.getName()))
				continue;

			// Parameters not element equal, no match
			if (!elementsEqual(transform(item.getTypeParameters(), MegalEntity::getName), Splitter.on(',')
					.omitEmptyStrings().split(entity.getAnnotation(PARAMS, ""))))
				continue;

			return item;
		}

		if (transitive)
			for (MegalFile imported : reverse(megamodel.getImports())) {
				MegalEntity potential = resolve(transitive, imported, entity);
				if (potential != null)
					return potential;
			}

		return null;
	}

	/**
	 * <p>
	 * Resolves to the corresponding relationship type in the MegaL notation
	 * </p>
	 * 
	 * @param megamodel
	 *            The megamodel in MegaL notation
	 * @param relationshipType
	 *            The item to resolve
	 * @return The resolved item
	 */
	public static MegalRelationshipType resolve(boolean transitive, MegalFile megamodel,
			RelationshipType relationshipType) {
		for (MegalRelationshipType item : from(megamodel.getDeclarations()).filter(MegalRelationshipType.class)) {
			// Not equal name, no match
			if (!equal(item.getName(), relationshipType.getName()))
				continue;

			// Left type not equal, no match
			if (!equal(item.getTypeLeft().getName(), relationshipType.getLeft().getName()))
				continue;

			// Left parameters not element equal, no match
			if (!elementsEqual(transform(item.getTypeLeftParameters(), MegalEntity::getName), Splitter.on(',')
					.omitEmptyStrings().split(relationshipType.getAnnotation(PARAMS_LEFT, ""))))
				continue;

			// Right type not equal, no match
			if (!equal(item.getTypeRight().getName(), relationshipType.getRight().getName()))
				continue;

			// Right parameters not element equal, no match
			if (!elementsEqual(transform(item.getTypeRightParameters(), MegalEntity::getName), Splitter.on(',')
					.omitEmptyStrings().split(relationshipType.getAnnotation(PARAMS_RIGHT, ""))))
				continue;

			return item;
		}

		if (transitive)
			for (MegalFile imported : reverse(megamodel.getImports())) {
				MegalRelationshipType potential = resolve(true, imported, relationshipType);
				if (potential != null)
					return potential;
			}

		return null;
	}

	/**
	 * <p>
	 * Resolves to the corresponding relationship in the MegaL notation
	 * </p>
	 * 
	 * @param megamodel
	 *            The megamodel in MegaL notation
	 * @param relationship
	 *            The item to resolve
	 * @return The resolved item
	 */
	public static MegalRelationship resolve(boolean transitive, MegalFile megamodel, Relationship relationship) {
		for (MegalRelationship item : from(megamodel.getDeclarations()).filter(MegalRelationship.class)) {
			// Left entity not equal, no match
			if (!equal(item.getLeft().getName(), relationship.getLeft().getName()))
				continue;

			// Relationship type not equal, no match
			if (!equal(item.getType().getName(), relationship.getType().getName()))
				continue;

			// Right entity not equal, no match
			if (!equal(item.getRight().getName(), relationship.getRight().getName()))
				continue;

			return item;
		}

		if (transitive)
			for (MegalFile imported : reverse(megamodel.getImports())) {
				MegalRelationship potential = resolve(transitive, imported, relationship);
				if (potential != null)
					return potential;
			}

		return null;
	}

	public static MegalFunctionApplication resolvePair(boolean transitive, MegalFile megamodel, Entity entity) {
		for (MegalFunctionApplication item : from(megamodel.getDeclarations()).filter(MegalFunctionApplication.class)) {
			// Not equal name, no match
			if (!equal(entityName(item.getFunction().getName(), item.getInput().getName(), item.getOutput().getName()),
					entity.getName()))
				continue;

			return item;
		}

		if (transitive)
			for (MegalFile imported : reverse(megamodel.getImports())) {
				MegalFunctionApplication potential = resolvePair(transitive, imported, entity);
				if (potential != null)
					return potential;
			}

		return null;
	}

	public static MegalFunctionApplication resolveFirst(boolean transitive, MegalFile megamodel,
			Relationship relationship) {
		for (MegalFunctionApplication item : from(megamodel.getDeclarations()).filter(MegalFunctionApplication.class)) {
			if (!equal(item.getInput().getName(), relationship.getLeft().getName()))
				continue;

			if (!equal(FIRST_OF, relationship.getType().getName()))
				continue;

			if (!equal(entityName(item.getFunction().getName(), item.getInput().getName(), item.getOutput().getName()),
					relationship.getRight().getName()))
				continue;

			return item;
		}

		if (transitive)
			for (MegalFile imported : reverse(megamodel.getImports())) {
				MegalFunctionApplication potential = resolveFirst(transitive, imported, relationship);
				if (potential != null)
					return potential;
			}

		return null;
	}

	public static MegalFunctionApplication resolveSecond(boolean transitive, MegalFile megamodel,
			Relationship relationship) {
		for (MegalFunctionApplication item : from(megamodel.getDeclarations()).filter(MegalFunctionApplication.class)) {
			if (!equal(item.getInput().getName(), relationship.getLeft().getName()))
				continue;

			if (!equal(SECOND_OF, relationship.getType().getName()))
				continue;

			if (!equal(entityName(item.getFunction().getName(), item.getInput().getName(), item.getOutput().getName()),
					relationship.getRight().getName()))
				continue;

			return item;
		}

		if (transitive)
			for (MegalFile imported : reverse(megamodel.getImports())) {
				MegalFunctionApplication potential = resolveSecond(transitive, imported, relationship);
				if (potential != null)
					return potential;
			}

		return null;
	}

	public static MegalFunctionApplication resolveElement(boolean transitive, MegalFile megamodel,
			Relationship relationship) {
		for (MegalFunctionApplication item : from(megamodel.getDeclarations()).filter(MegalFunctionApplication.class)) {
			if (!equal(entityName(item.getFunction().getName(), item.getInput().getName(), item.getOutput().getName()),
					relationship.getLeft().getName()))
				continue;

			if (!equal(ELEMENT_OF, relationship.getType().getName()))
				continue;

			if (!equal(item.getFunction().getName(), relationship.getRight().getName()))
				continue;

			return item;
		}

		if (transitive)
			for (MegalFile imported : reverse(megamodel.getImports())) {
				MegalFunctionApplication potential = resolveElement(transitive, imported, relationship);
				if (potential != null)
					return potential;
			}

		return null;
	}

	private static String entityName(String function, String input, String output) {
		return "(" + input + ", " + output + ") in " + function;
	}

	/**
	 * <p>
	 * Internal backing field.
	 * </p>
	 */
	private final MegalFile megamodel;

	/**
	 * <p>
	 * Internal backing field.
	 * </p>
	 */
	private final String title;

	/**
	 * <p>
	 * Internal backing field.
	 * </p>
	 */
	private final SetMultimap<String, String> annotations;

	/**
	 * <p>
	 * Internal backing field.
	 * </p>
	 */
	private final SetMultimap<String, String> theEntityTypeAnnotations;

	/**
	 * <p>
	 * Internal backing field.
	 * </p>
	 */
	private final Map<String, String> entityTypes;

	/**
	 * <p>
	 * Internal backing field.
	 * </p>
	 */
	private final Multitable<Ref, Ref, String> relationshipTypes;

	/**
	 * <p>
	 * Internal backing field.
	 * </p>
	 */
	private final Map<String, Ref> entities;

	/**
	 * <p>
	 * Internal backing field.
	 * </p>
	 */
	private final Map<String, Object> bindings;

	/**
	 * <p>
	 * Internal backing field.
	 * </p>
	 */
	private final Multitable<String, String, String> relationships;

	/**
	 * <p>
	 * Internal backing field.
	 * </p>
	 */
	private final SetMultimap<Entry<String, String>, Entry<String, String>> entityTypeAnnotations;

	/**
	 * <p>
	 * Internal backing field.
	 * </p>
	 */
	private final SetMultimap<Cell<Ref, Ref, String>, Entry<String, String>> relationshipTypeAnnotations;

	/**
	 * <p>
	 * Internal backing field.
	 * </p>
	 */
	private final SetMultimap<Entry<String, Ref>, Entry<String, String>> entityAnnotations;

	/**
	 * <p>
	 * Internal backing field.
	 * </p>
	 */
	private final SetMultimap<Cell<String, String, String>, Entry<String, String>> relationshipAnnotations;

	private static Multimap<String, String> getAnnotationMap(MegalElement element) {
		return transformValues(index(element.getAnnotations(), MegalAnnotation::getKey), MegalAnnotation::getValue);
	}

	/**
	 * <p>
	 * Initialized and analyzes the given megamodel.
	 * </p>
	 * 
	 * @param megamodel
	 *            The given megamodel
	 */
	public MegamodelKB(MegalFile megamodel) {
		this.megamodel = megamodel;

		// Create database
		title = megamodel.getName();
		annotations = HashMultimap.create(getAnnotationMap(megamodel));
		theEntityTypeAnnotations = HashMultimap.create();
		entityTypes = newHashMap();
		relationshipTypes = HashMultitable.create();
		entities = newHashMap();
		bindings = newHashMap();
		relationships = HashMultitable.create();
		entityTypeAnnotations = HashMultimap.create();
		relationshipTypeAnnotations = HashMultimap.create();
		entityAnnotations = HashMultimap.create();
		relationshipAnnotations = HashMultimap.create();

		// Initialize database
		for (MegalDeclaration declaration : megamodel.getDeclarations())
			if (declaration instanceof MegalEntityType) {
				MegalEntityType entityType = (MegalEntityType) declaration;

				if (entityType.getSupertype() == null)
					// Put the entity type annotations
					theEntityTypeAnnotations.putAll(getAnnotationMap(entityType));
				else {
					// Translate the entity type
					String name = entityType.getName();
					String supertype = entityType.getSupertype().getName();

					// Put the data
					entityTypes.put(name, supertype);

					// Put the annotations
					entityTypeAnnotations.putAll(immutableEntry(name, supertype), getAnnotationMap(entityType)
							.entries());
				}

			} else if (declaration instanceof MegalRelationshipType) {
				MegalRelationshipType relationshipType = (MegalRelationshipType) declaration;

				// Get the names of the types
				String leftTypeName = relationshipType.getTypeLeft().getName();
				String rightTypeName = relationshipType.getTypeRight().getName();

				// Get the 'many' expansion set for the relationship domain and
				// codomain
				List<Boolean> leftTypeManys = relationshipType.isTypeLeftArb() ? ImmutableList.of(false, true)
						: relationshipType.isTypeLeftMany() ? ImmutableList.of(true) : ImmutableList.of(false);

				List<Boolean> rightTypeManys = relationshipType.isTypeRightArb() ? ImmutableList.of(false, true)
						: relationshipType.isTypeRightMany() ? ImmutableList.of(true) : ImmutableList.of(false);

				// Get the params of the types
				List<String> leftTypeParams = transform(relationshipType.getTypeLeftParameters(), MegalEntity::getName);
				List<String> rightTypeParams = transform(relationshipType.getTypeRightParameters(),
						MegalEntity::getName);

				for (boolean leftMany : leftTypeManys)
					for (boolean rightMany : rightTypeManys) {
						// Translate the relationship type
						String name = relationshipType.getName();
						Ref left = Ref.to(leftTypeName, leftMany);
						Ref right = Ref.to(rightTypeName, rightMany);

						// Put the data
						relationshipTypes.put(left, right, name);

						// Create the address of the data
						Cell<Ref, Ref, String> cell = immutableCell(left, right, name);

						// Put the annotations
						relationshipTypeAnnotations.putAll(cell, getAnnotationMap(relationshipType).entries());

						if (!leftTypeParams.isEmpty())
							// Put left parameters if present
							relationshipTypeAnnotations.put(cell,
									immutableEntry(PARAMS_LEFT, Joiner.on(',').join(leftTypeParams)));

						if (!rightTypeParams.isEmpty())
							// Put right parameters if present
							relationshipTypeAnnotations.put(cell,
									immutableEntry(PARAMS_RIGHT, Joiner.on(',').join(rightTypeParams)));
					}

			} else if (declaration instanceof MegalEntity) {
				MegalEntity entity = (MegalEntity) declaration;

				// Get the type properties
				String typeName = entity.getType().getName();
				boolean typeMany = entity.isTypeMany();
				List<String> typeParams = transform(entity.getTypeParameters(), MegalEntity::getName);

				// Translate the entity
				String name = entity.getName();
				Ref ref = Ref.to(typeName, typeMany);

				// Put the data
				entities.put(name, ref);

				// Create the address of the data
				Entry<String, Ref> entry = immutableEntry(name, ref);

				// Put the annotations
				entityAnnotations.putAll(entry, getAnnotationMap(entity).entries());

				if (!typeParams.isEmpty())
					// Put parameters if present
					entityAnnotations.put(entry, immutableEntry(PARAMS, Joiner.on(',').join(typeParams)));

			} else if (declaration instanceof MegalRelationship) {
				MegalRelationship relationship = (MegalRelationship) declaration;

				// Translate the relationship
				String name = relationship.getType().getName();
				String left = relationship.getLeft().getName();
				String right = relationship.getRight().getName();

				// Put the data
				relationships.put(left, right, name);

				// Put the annotations
				relationshipAnnotations.putAll(immutableCell(left, right, name), getAnnotationMap(relationship)
						.entries());
			} else if (declaration instanceof MegalFunctionApplication) {
				MegalFunctionApplication functionApplication = (MegalFunctionApplication) declaration;

				// Translate the function application
				String function = functionApplication.getFunction().getName();
				String first = functionApplication.getInput().getName();
				String second = functionApplication.getOutput().getName();

				String name = entityName(function, first, second);
				Ref type = Ref.to(PAIR, false);

				// Put the entity
				entities.put(name, type);

				// Put all relationships
				relationships.put(name, function, ELEMENT_OF);
				relationships.put(first, name, FIRST_OF);
				relationships.put(second, name, SECOND_OF);

				// Put the annotations
				relationshipAnnotations.putAll(immutableCell(name, function, ELEMENT_OF),
						getAnnotationMap(functionApplication).entries());
				relationshipAnnotations.putAll(immutableCell(first, name, FIRST_OF),
						getAnnotationMap(functionApplication).entries());
				relationshipAnnotations.putAll(immutableCell(second, name, SECOND_OF),
						getAnnotationMap(functionApplication).entries());
				entityAnnotations.putAll(immutableEntry(name, type), getAnnotationMap(functionApplication).entries());
			}

		// Continue with bindings
		for (MegalLink link : megamodel.getBindings())
			if (link.getInput() != null && link.getOutput() != null) {
				// Linking a function application, so restore the target
				String function = link.getLink().getName();
				String input = link.getInput().getName();
				String output = link.getOutput().getName();

				// Compute data
				String name = entityName(function, input, output);
				String to = link.getTo();

				// Put binding
				bindings.put(name, to);

			} else {
				// Get data
				String name = link.getLink().getName();
				String to = link.getTo();

				// Put binding
				bindings.put(name, to);
			}
	}

	public static KB loadAll(MegalFile m) {
		// Simple model, most likely the prelude
		if (m.getImports().size() == 0)
			return new MegamodelKB(m);

		// Translate all imported megamodels recursively
		KB[] imports = new KB[m.getImports().size()];
		for (int i = 0; i < m.getImports().size(); i++)
			imports[i] = loadAll(m.getImports().get(i));

		// Reduce with union
		KB rh = imports[0];
		for (int i = 1; i < imports.length; i++)
			rh = KBs.union(imports[i], rh);

		// Union with the main model
		return KBs.union(new MegamodelKB(m), rh);
	}

	/**
	 * <p>
	 * Gets the input megamodel in MegaL notation.
	 * </p>
	 * 
	 * @return Returns the input
	 */
	public MegalFile getMegamodel() {
		return megamodel;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public SetMultimap<String, String> getRawAnnotations() {
		return annotations;
	}

	@Override
	public SetMultimap<String, String> getRawTheEntityTypeAnnotations() {
		return theEntityTypeAnnotations;
	}

	@Override
	public Map<String, String> getRawEntityTypes() {
		return entityTypes;
	}

	@Override
	public Multitable<Ref, Ref, String> getRawRelationshipTypes() {
		return relationshipTypes;
	}

	@Override
	public Map<String, Ref> getRawEntities() {
		return entities;
	}

	@Override
	public Map<String, Object> getRawBindings() {
		return bindings;
	}

	@Override
	public Multitable<String, String, String> getRawRelationships() {
		return relationships;
	}

	@Override
	public SetMultimap<Entry<String, String>, Entry<String, String>> getRawEntityTypeAnnotations() {
		return entityTypeAnnotations;
	}

	@Override
	public SetMultimap<Cell<Ref, Ref, String>, Entry<String, String>> getRawRelationshipTypeAnnotations() {
		return relationshipTypeAnnotations;
	}

	@Override
	public SetMultimap<Entry<String, Ref>, Entry<String, String>> getRawEntityAnnotations() {
		return entityAnnotations;
	}

	@Override
	public SetMultimap<Cell<String, String, String>, Entry<String, String>> getRawRelationshipAnnotations() {
		return relationshipAnnotations;
	}
}
