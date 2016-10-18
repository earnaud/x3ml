/*==============================================================================
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
==============================================================================*/
package gr.forth;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import static eu.delving.x3ml.X3MLGeneratorPolicy.CustomGenerator;
import static eu.delving.x3ml.X3MLGeneratorPolicy.CustomGeneratorException;

/** The URIorUUID generator is responsible for generating a URI and in the cases 
 * where it is not valid it generates a UUID. It takes a single parameter (with name "text") 
 * which can be given either from the input, or through a constant. If the given value is not 
 * a valid URI or URN, it generates a UUID. 
 * 
 * @author Nikos Minadakis &lt;minadakn@ics.forth.gr&gt;
 * @author Yannis Marketakis &lt;marketak@ics.forth.gr&gt;
 */
public class URIorUUID implements CustomGenerator {
    private String text;

    /** Sets the value of the argument with the given value.
     * 
     * @param name the name of the argument (the generator recognizes the "text" argument)
     * @param value the value of the corresponding argument
     * @throws CustomGeneratorException it is thrown if the name of the argument is different 
     * from the expected one ("text") */
    @Override
    public void setArg(String name, String value) throws CustomGeneratorException {
        if(Labels.TEXT.equals(name)){
            text = value;
        }else{
            throw new CustomGeneratorException("Unrecognized argument name: " + name);
        }
    }

    /** Returns the value of the URIorUUID generator.
     * 
     * @return the value of the given generator
     * @throws CustomGeneratorException if the argument of the generator is missing or null*/
    @Override
    public String getValue() throws CustomGeneratorException {
        if(text == null){
            throw new CustomGeneratorException("Missing text argument");
        }
        return text;
    }

    /** Returns the type of the generated value. The generator is responsible for constructing 
     * identifiers, therefore it is expected to return either a URI or a UUID.
     * 
     * @return the type of the generated value (i.e. URI or UUID)
     * @throws CustomGeneratorException if the argument is missing or null */
    @Override
    public String getValueType() throws CustomGeneratorException {
        if(text == null){
            throw new CustomGeneratorException("Missing text argument");
        }
        return isValidURL(text) || isValidURN(text) ? Labels.URI : Labels.UUID;
    }

    /* Checks of the given string corresponds to a valid URL */
    private boolean isValidURL(String urlString) {
        URL url;
        try{
            url = new URL(urlString);
            url.toURI();
            return true;
        }catch(MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
    
    /* Checks if the given string corresponds to a valid URN */
    private boolean isValidURN(String urnString) {
        return urnString.startsWith(Labels.URN+":")||urnString.startsWith(Labels.URN+":".toLowerCase());
    }
}