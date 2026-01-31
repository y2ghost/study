package study.ywork.basis.security;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.CodeSigner;
import java.security.Timestamp;
import java.security.cert.CertPath;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Date;

public class CodeSignDemo {
	public static void main(String[] args) throws CertificateException, FileNotFoundException {
		CertificateFactory cf = CertificateFactory.getInstance("X509");
		CertPath cp = cf.generateCertPath(new FileInputStream("test.cer"));
		Timestamp t = new Timestamp(new Date(), cp);
		CodeSigner cs = new CodeSigner(cp, t);
		boolean status = cs.equals(new CodeSigner(cp, t));
		System.out.println(status);
	}
}
