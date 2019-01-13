package org.enkrip.pdfsign.storage;

import org.immutables.value.Value;

@Value.Immutable
public interface HashRestData {
	String getHash();
}
