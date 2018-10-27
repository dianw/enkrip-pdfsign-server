package org.enkrip.pdfsign.hash;

import org.immutables.value.Value;

@Value.Immutable
public interface HashRestData {
	String getHash();
}
