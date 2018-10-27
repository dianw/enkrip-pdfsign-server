package org.enkrip.pdfsign.signature;

import java.time.Instant;
import java.util.Set;

import javax.annotation.Nullable;

import org.immutables.value.Value;

@Value.Immutable
public interface SignatureRestData {
	Set<X509CertificateRestData> getCertificates();

	@Nullable
	String getLocation();

	@Nullable
	String getName();

	@Nullable
	String getReason();

	@Nullable
	Instant getSignDate();
}
