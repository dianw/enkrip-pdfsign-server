package org.enkrip.pdfsign.signature;

import org.immutables.value.Value;

@Value.Immutable
public interface X500PrincipalRestData {
	byte[] getEncoded();

	String getName();
}
