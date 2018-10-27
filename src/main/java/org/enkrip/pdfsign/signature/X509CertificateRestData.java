package org.enkrip.pdfsign.signature;

import java.math.BigInteger;
import java.util.Date;

import javax.annotation.Nullable;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonProperty;

@Value.Immutable
public interface X509CertificateRestData {
	int getBasicConstraints();

	@JsonProperty("issuerDN")
	String getCertIssuerDN();

	@Nullable
	@JsonProperty("issuerUniqueID")
	boolean[] getCertIssuerUniqueID();

	@JsonProperty("issuerX500Principal")
	X500PrincipalRestData getCertIssuerX500Principal();

	byte[] getEncoded();

	boolean[] getKeyUsage();

	Date getNotAfter();

	Date getNotBefore();

	PublicKeyRestData getPublicKey();

	BigInteger getSerialNumber();

	String getSigAlgName();

	String getSigAlgOID();

	@Nullable
	byte[] getSigAlgParams();

	byte[] getSignature();

	String getSubjectDN();

	@Nullable
	boolean[] getSubjectUniqueID();

	X500PrincipalRestData getSubjectX500Principal();

	int getVersion();
}
