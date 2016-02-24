package de.repictures.diewitzeapp.dws;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.util.logging.Logger;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "dwspostApi",
        version = "v1",
        resource = "dwspost",
        namespace = @ApiNamespace(
                ownerDomain = "dws.diewitzeapp.repictures.de",
                ownerName = "dws.diewitzeapp.repictures.de",
                packagePath = ""
        )
)
public class dwspostEndpoint {

    private static final Logger logger = Logger.getLogger(dwspostEndpoint.class.getName());

    /**
     * This method gets the <code>dwspost</code> object associated with the specified <code>id</code>.
     *
     * @param id The id of the object to be returned.
     * @return The <code>dwspost</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getdwspost")
    public dwspost getdwspost(@Named("id") Long id) {
        logger.info("Calling getdwspost method");
        return null;
    }

    /**
     * This inserts a new <code>dwspost</code> object.
     *
     * @param dwspost The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertdwspost")
    public dwspost insertdwspost(dwspost dwspost) {
        logger.info("Calling insertdwspost method");
        return dwspost;
    }
}