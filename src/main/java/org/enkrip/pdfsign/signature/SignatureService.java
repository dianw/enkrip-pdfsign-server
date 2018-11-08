package org.enkrip.pdfsign.signature;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.List;

import org.bouncycastle.cms.CMSException;

public interface SignatureService {
	List<SignatureRestData> getSignatures(String hash) throws IOException, CMSException, CertificateException;
}
