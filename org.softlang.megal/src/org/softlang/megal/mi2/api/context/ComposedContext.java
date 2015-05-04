package org.softlang.megal.mi2.api.context;

import java.net.URI;

import org.softlang.megal.mi2.KB;
import org.softlang.megal.mi2.api.Message;
import org.softlang.megal.mi2.api.emission.Emission;
import org.softlang.megal.mi2.api.resolution.Resolution;
import org.softlang.sourcesupport.SourceSupport;

import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;

public final class ComposedContext implements Context {
	private final Resolution resolutionDelegate;

	private final Emission emissionDelegate;

	public ComposedContext(Resolution resolutionDelegate, Emission emissionDelegate) {
		this.resolutionDelegate = resolutionDelegate;
		this.emissionDelegate = emissionDelegate;
	}

	@Override
	public void emit(Message message) {
		emissionDelegate.emit(message);
	}

	@Override
	public SourceSupport getSourceSupport() {
		return resolutionDelegate.getSourceSupport();
	}

	@Override
	public <T> Class<? extends T> getClass(Object object, Class<T> deriving) {
		return resolutionDelegate.getClass(object, deriving);
	}

	@Override
	public URI getAbsolute(Object object) {
		return resolutionDelegate.getAbsolute(object);
	}

	@Override
	public CharSource getChars(Object object) {
		return resolutionDelegate.getChars(object);
	}

	@Override
	public ByteSource getBytes(Object object) {
		return resolutionDelegate.getBytes(object);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + emissionDelegate.hashCode();
		result = prime * result + resolutionDelegate.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComposedContext other = (ComposedContext) obj;
		if (!emissionDelegate.equals(other.emissionDelegate))
			return false;
		if (!resolutionDelegate.equals(other.resolutionDelegate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ComposedContext [TresolutionDelegate=" + resolutionDelegate + ", emissionDelegate=" + emissionDelegate
				+ "]";
	}

}