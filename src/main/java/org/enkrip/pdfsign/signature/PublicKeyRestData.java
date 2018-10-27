package org.enkrip.pdfsign.signature;

import org.immutables.value.Value;

@Value.Immutable
public interface PublicKeyRestData {
	String getAlgorithm();

	byte[] getEncoded();

	String getFormat();
}
