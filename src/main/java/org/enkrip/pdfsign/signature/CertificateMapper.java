package org.enkrip.pdfsign.signature;

import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Calendar;
import java.util.Set;

import javax.security.auth.x500.X500Principal;

import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CertificateMapper {
	CertificateMapper MAPPER = Mappers.getMapper(CertificateMapper.class);

	default Instant toInstant(Calendar calendar) {
		return calendar.toInstant();
	}

	SignatureRestData toRestData(PDSignature signature, Set<X509Certificate> certificates);

	@Mapping(source = "issuerDN.name", target = "certIssuerDN")
	@Mapping(source = "issuerUniqueID", target = "certIssuerUniqueID")
	@Mapping(source = "issuerX500Principal", target = "certIssuerX500Principal")
	@Mapping(source = "subjectDN.name", target = "subjectDN")
	X509CertificateRestData toRestData(X509Certificate certificate) throws CertificateEncodingException;

	X500PrincipalRestData toRestData(X500Principal principal);

	PublicKeyRestData toRestData(PublicKey publicKey);
}
