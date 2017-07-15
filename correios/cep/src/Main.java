import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;

import javax.xml.rpc.ServiceException;

import br.com.correios.bsb.sigep.master.bean.cliente.AtendeCliente;
import br.com.correios.bsb.sigep.master.bean.cliente.AtendeClienteServiceLocator;
import br.com.correios.bsb.sigep.master.bean.cliente.EnderecoERP;
import br.com.correios.bsb.sigep.master.bean.cliente.SQLException;
import br.com.correios.bsb.sigep.master.bean.cliente.SigepClienteException;

public class Main {
	private static FileWriter out;
	private static FileWriter err;

	public static void main(String[] args) throws SQLException,
			SigepClienteException, RemoteException, ServiceException,
			FileNotFoundException, IOException {

		out = new FileWriter("out.txt", false);
		err = new FileWriter("err.txt", false);

		AtendeClienteServiceLocator locator = new AtendeClienteServiceLocator();
		AtendeCliente service = locator.getAtendeClientePort();

		DecimalFormat format = new DecimalFormat("00000");

		for (long i = 45095; i <= 99999999; i++) {

			String input = format.format(i);
			EnderecoERP result = null;
			try {
				result = service.consultaCEP(input);
				appendResult(input, result);
			} catch (SigepClienteException exception) {
				appendError(input, exception);
			}
			System.out.println(input);
		}
	}

	private static void appendResult(String input, EnderecoERP result)
			throws IOException {
		append(input);
		append(result.getId());
		append(result.getCep());
		append(result.getEnd());
		append(result.getComplemento());
		append(result.getComplemento2());
		append(result.getBairro());
		append(result.getCidade());
		append(result.getUf());
		appendLine();
	}

	private static void appendError(String input,
			SigepClienteException exception) throws IOException {
		appendErr(input);
		appendErr(exception.getClass().getName());
		appendErr(exception.getSigepClienteException());
		appendErrLine();
	}

	private static void append(Object object) throws IOException {
		if (object != null) {
			out.write(String.valueOf(object));
		}
		out.write('\t');
	}

	private static void appendLine() throws IOException {
		out.write('\n');
		out.flush();
	}

	private static void appendErr(Object object) throws IOException {
		if (object != null) {
			err.write(String.valueOf(object));
		}
		err.write('\t');
	}

	private static void appendErrLine() throws IOException {
		err.write('\n');
		err.flush();
	}

}
