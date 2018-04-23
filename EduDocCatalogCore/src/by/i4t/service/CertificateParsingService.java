package by.i4t.service;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.util.Store;

/**
 * Personal certificate parsing service.
 */
public class CertificateParsingService {
    /**
     * Personal certificate parsing service.
     *
     * @param inputStream
     * @return params map:
     * key: 'certificatId', value: number of the personal certificate;
     * key: 'userName', value: user name from personal certificate;
     * @throws CMSException
     */
    public static Map<String, String> parse(InputStream inputStream) throws CMSException {
        Map<String, String> paramsMap = new HashMap<>();
        CMSSignedData sd = new CMSSignedData(inputStream);
        Store store = sd.getCertificates();
        Collection<X509CertificateHolder> certificates = store.getMatches(null);
        for (X509CertificateHolder cert : certificates) {
            if (!cert.getSubject().equals(cert.getIssuer())) {
                paramsMap.put("certificatId", cert.getSerialNumber().toString(16).toUpperCase());

                RDN[] rdns = cert.getSubject().getRDNs(new ASN1ObjectIdentifier("2.5.4.3"));
                paramsMap.put("userName", rdns[0].getFirst().getValue().toString());
            }
        }
        return paramsMap;
    }
}
