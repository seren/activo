// reasoner

package com.fourhcistudents.activo;

/**
 * Created by seren on 2/10/15.
 */


import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.engine.iterator.QueryIterDistinguishedVars;
import com.hp.hpl.jena.rdf.model.*;



public class ModalityChooser {
    private Model model = ModelFactory.createDefaultModel();

    ModalityChooser(Context context) throws FileNotFoundException {
        //log init
//        org.apache.log4j.BasicConfigurator.configure();

        //load model
        InputStream f;
        try {
//            BufferedReader reader = new BufferedReader(
//                new InputStreamReader(contex.getAssets().open("all.rdf")));
            f = context.getAssets().open("all.rdf");
            model.read(f, "http://imi.org/");
        } catch (IOException e) {
            Log.d("SpeechRepeatActivity", e.toString());
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



//    public HashMap<String, String> getModalities(HashMap systemSettings) {
    public String[] getModalities(HashMap systemSettings) {
        //Sparql
        Query query = QueryFactory.create("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX ex: <http://imi.org/> " +
                        "PREFIX imi:   <http://intelligent-multimodal-interaction.org/relations/> " +
                        "SELECT ?modality \n" +
                        "    WHERE { \n" +
                        "CurrentSituation UserActivity ?act .\n" +
                        "        ?act requires ?modality .\n" +
                        "CurrentSituation PhoneLocation ?loc .\n" +
                        "        ?loc requires ?modality .\n" +
                        "CurrentSituation PhoneMode ?mode .\n" +
                        "        ?mode requires ?modality .\n" +
                        "FILTER ( ?act  = <"+systemSettings.get("UserActivity")+">) .\n" +
                        "FILTER ( ?loc  = <"+systemSettings.get("PhoneLocation")+">) .\n" +
                        "FILTER ( ?mode = <"+systemSettings.get("PhoneMode")+">)\n");


        QueryExecution qe = QueryExecutionFactory.create(query, this.model);
        ResultSet rs = qe.execSelect();

//        String[] modalityPreference;
        HashMap<String, Integer> modalityPreference = new HashMap<String, Integer>();
        while(rs.hasNext())
        {
            QuerySolution sol = rs.next();
            Resource resmodality = sol.getResource("modality");
            String modality = resmodality.toString();
            if(modalityPreference.containsKey(modality)){
                modalityPreference.put(modality, modalityPreference.get(modality) + 1);
            } else {
                modalityPreference.put(modality, 1);
            }
        }

        qe.close();

        return (String[]) modalityPreference.keySet().toArray();
    }
}

