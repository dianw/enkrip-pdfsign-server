package org.enkrip.pdfsign.signature;

import java.util.List;

public interface SignatureService {
	List<SignatureRestData> getSignatures(String hash);
}
