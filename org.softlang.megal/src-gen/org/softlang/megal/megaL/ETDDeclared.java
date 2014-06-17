/**
 */
package org.softlang.megal.megaL;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ETD Declared</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.softlang.megal.megaL.ETDDeclared#getReference <em>Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.softlang.megal.megaL.MegaLPackage#getETDDeclared()
 * @model
 * @generated
 */
public interface ETDDeclared extends ETDR
{
  /**
   * Returns the value of the '<em><b>Reference</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Reference</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Reference</em>' reference.
   * @see #setReference(ETD)
   * @see org.softlang.megal.megaL.MegaLPackage#getETDDeclared_Reference()
   * @model
   * @generated
   */
  ETD getReference();

  /**
   * Sets the value of the '{@link org.softlang.megal.megaL.ETDDeclared#getReference <em>Reference</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Reference</em>' reference.
   * @see #getReference()
   * @generated
   */
  void setReference(ETD value);

} // ETDDeclared
