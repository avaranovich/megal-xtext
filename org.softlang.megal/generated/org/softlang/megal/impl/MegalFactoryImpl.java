/**
 */
package org.softlang.megal.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.softlang.megal.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MegalFactoryImpl extends EFactoryImpl implements MegalFactory
{
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MegalFactory init()
	{
		try
		{
			MegalFactory theMegalFactory = (MegalFactory)EPackage.Registry.INSTANCE.getEFactory(MegalPackage.eNS_URI);
			if (theMegalFactory != null)
			{
				return theMegalFactory;
			}
		}
		catch (Exception exception)
		{
			EcorePlugin.INSTANCE.log(exception);
		}
		return new MegalFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MegalFactoryImpl()
	{
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass)
	{
		switch (eClass.getClassifierID())
		{
			case MegalPackage.MEGAMODEL: return createMegamodel();
			case MegalPackage.ENTITY_TYPE: return createEntityType();
			case MegalPackage.RELATIONSHIP_TYPE: return createRelationshipType();
			case MegalPackage.ENTITY: return createEntity();
			case MegalPackage.RELATIONSHIP: return createRelationship();
			case MegalPackage.LINK: return createLink();
			case MegalPackage.ENTITY_TYPE_REFERENCE: return createEntityTypeReference();
			case MegalPackage.ANNOTATION: return createAnnotation();
			case MegalPackage.IMPORT: return createImport();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Megamodel createMegamodel()
	{
		MegamodelImpl megamodel = new MegamodelImpl();
		return megamodel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityType createEntityType()
	{
		EntityTypeImpl entityType = new EntityTypeImpl();
		return entityType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RelationshipType createRelationshipType()
	{
		RelationshipTypeImpl relationshipType = new RelationshipTypeImpl();
		return relationshipType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Entity createEntity()
	{
		EntityImpl entity = new EntityImpl();
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Relationship createRelationship()
	{
		RelationshipImpl relationship = new RelationshipImpl();
		return relationship;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Link createLink()
	{
		LinkImpl link = new LinkImpl();
		return link;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityTypeReference createEntityTypeReference()
	{
		EntityTypeReferenceImpl entityTypeReference = new EntityTypeReferenceImpl();
		return entityTypeReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Annotation createAnnotation()
	{
		AnnotationImpl annotation = new AnnotationImpl();
		return annotation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Import createImport()
	{
		ImportImpl import_ = new ImportImpl();
		return import_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MegalPackage getMegalPackage()
	{
		return (MegalPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static MegalPackage getPackage()
	{
		return MegalPackage.eINSTANCE;
	}

} //MegalFactoryImpl
