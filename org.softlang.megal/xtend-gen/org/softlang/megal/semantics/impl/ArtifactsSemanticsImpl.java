package org.softlang.megal.semantics.impl;

import com.google.common.base.Optional;
import java.io.File;
import org.softlang.megal.megaL.ED;
import org.softlang.megal.megaL.LD;
import org.softlang.megal.semantics.Diagnostic;
import org.softlang.megal.semantics.EntitySemantics;

@SuppressWarnings("all")
public class ArtifactsSemanticsImpl implements EntitySemantics {
  public void validate(final Diagnostic diagnostic, final ED entity, final Optional<LD> linking) {
    boolean _isPresent = linking.isPresent();
    if (_isPresent) {
      LD _get = linking.get();
      String _value = _get.getValue();
      File _file = new File(_value);
      boolean _exists = _file.exists();
      boolean _not = (!_exists);
      if (_not) {
        diagnostic.error("File does not exist");
      }
    }
  }
}