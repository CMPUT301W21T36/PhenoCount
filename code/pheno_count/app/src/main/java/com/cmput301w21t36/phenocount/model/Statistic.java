package com.cmput301w21t36.phenocount;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This model class deals with displaying statistics for the trials of an experiment
 */
public class Statistic {

    int numOfSuccess = 0;
    double mean = 0.0;
    double median = 0.0;
    double sd = 0.0;
    double q1 = 0.0;
    double q3 = 0.0;
    int count = 0;
    int value = 0;
    float measurement=0;

    public double getMean(ArrayList<Trial> trials, String expType){
        if (expType.equals("Binomial")){
            for(Trial trial : trials){
                Binomial btrial = (Binomial) trial;
                if(btrial.getResult()){
                    numOfSuccess++;
                }
            }
            mean = (double) numOfSuccess/(double) trials.size();
        }
        if(expType.equals("Count")){
            for(Trial trial : trials){
                Count ctrial = (Count) trial;
                count = count + ctrial.getCount();
            }
            mean = (double)count/(double)trials.size();
        }
        if (expType.equals("NonNegativeCount")){
            for(Trial trial : trials){
                NonNegativeCount ntrial = (NonNegativeCount) trial;
                value = value + ntrial.getValue();
            }
            mean = (double)value/(double)trials.size();
        }
        if (expType.equals("Measurement")){
            for(Trial trial : trials){
                Measurement mtrial = (Measurement) trial;
                measurement = measurement + mtrial.getMeasurement();
            }
            mean = (double)measurement/(double)trials.size();
        }
        return mean;
    }

    public double getMedian(ArrayList<Trial> trials,String expType){
        ArrayList<Integer> intList = new ArrayList<>();
        ArrayList<Float> floatList = new ArrayList<>();
        int size = trials.size();
        if (expType.equals("Binomial")){
            for(Trial trial : trials){
                Binomial btrial = (Binomial) trial;
                if(btrial.getResult()){
                    intList.add(1);
                }
                else {
                    intList.add(0);
                }
            }
        }
        if(expType.equals("Count")){
            for(Trial trial : trials){
                Count ctrial = (Count) trial;
                intList.add(ctrial.getCount());
            }
        }
        if (expType.equals("NonNegativeCount")){
            for(Trial trial : trials){
                NonNegativeCount ntrial = (NonNegativeCount) trial;
                intList.add(ntrial.getValue());
            }
        }
        if (!intList.isEmpty()){
            Collections.sort(intList);
            //calculating st dev
            for (int num : intList){
                sd = sd + Math.pow(num - mean,2);
            }
            sd = Math.sqrt(sd/(double) intList.size());
            //calculating Q1
            q1 = intList.get((intList.size())/4);
            //calculating Q3
            q3 = intList.get(((3*(intList.size()))/4));
            //q3 = 1.0;
            if (size % 2 != 0) {
                //if list size is odd
                median = (double) intList.get(size / 2);
            }
            //if list size is even
            median = (double)(intList.get((size - 1) / 2) + intList.get(size / 2)) / 2.0;
        }

        if (expType.equals("Measurement")){
            for(Trial trial : trials){
                Measurement mtrial = (Measurement) trial;
                floatList.add(mtrial.getMeasurement());
            }
            Collections.sort(floatList);
            //calculating st dev
            System.out.println("MEAN:"+mean);
            for (float num : floatList){
                sd = sd + Math.pow(num - mean,2);
            }
            sd = Math.sqrt(sd/(double) floatList.size());
            //calculating Q1
            q1 = floatList.get((floatList.size()+1)/4);
            //calculating Q3
            q3 = floatList.get((3*(floatList.size()))/4);
            if (size % 2 != 0)
                //if list size is odd
                return floatList.get(size/ 2);
            //if list size is even
            return (floatList.get((size - 1) / 2) + floatList.get(size / 2)) / 2.0;
        }
        return median;
    }

    public double getQ1(){
        return q1;
    }
    public double getQ3(){
        return q3;
    }
    public double getSd(){
        return sd;
    }

}
