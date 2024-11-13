    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package ejb.session.ws;

import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.Partner;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;

/**
 *
 * @author kevinlim
 */
@WebService(serviceName = "PartnerWebService")
@Stateless()
public class PartnerWebService {

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "partnerLogIn")
    public Partner partnerLogIn(@WebParam(name = "name") String name, 
                                    @WebParam(name = "password") String password) {
        return partnerSessionBean.partnerLogIn(name, password); 
    }
    
    
}
