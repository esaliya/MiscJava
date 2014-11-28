package org.saliya.streamprocessing;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;

import java.util.*;
import java.util.stream.Stream;

public class SpaceSavingConstTime {


    public static void main(String[] args) {
        int max = Integer.parseInt(args[0]);
        int k = Integer.parseInt(args[1]);

        /*int [] stream = new int[]{4,4,3,7,1,7,2,3};
        int [] stream = new int[]{3,2,7,1,4};
        max = stream.length;
        k = 3;
        Iterator<Integer> itr = Arrays.stream(stream).iterator();*/

                Iterator<Integer> itr = Stream.generate(Math::random).limit(max).mapToInt(
                        a -> ((Double) (a * 10)).intValue()).iterator(); // positive integer stream of values between 0 to 9 inclusive


        Hashtable<Integer, Integer> eToIdx = new Hashtable<>();
        Hashtable<Integer, Integer> feToCount = new Hashtable<>();
        Hashtable<Integer, int[]> feToRange = new Hashtable<>();

        ArrayEntry [] ssct = new ArrayEntry[k];// resultant array for the space saving constant algorithm
        int elementCount = 0;
        ArrayList<ArrayEntry> ssc = new ArrayList<>(k); // resultant array for the space saving classic algorithm

        while (itr.hasNext()){
            int n = itr.next();
//            System.out.println(n);
            elementCount = invokeSSCT(ssct, eToIdx,feToCount, feToRange, elementCount, n, k);
            invokeSSC(ssc, n, k);
        }

        System.out.println("classic" + Arrays.toString(ssc.toArray()));
        System.out.println("o(1)   " + Arrays.toString(ssct));

    }

    private static void invokeSSC(ArrayList<ArrayEntry> ssc, int n, int k) {
        ArrayEntry ae = new ArrayEntry(n);
        if (ssc.contains(ae)){
            int idx = ssc.indexOf(ae);
            ++ssc.get(idx).fe;
        } else {
            if (ssc.isEmpty()){
                ae.fe = 1;
                ssc.add(ae);
            } else {
                int sscSize = ssc.size();
                ArrayEntry sscLastEntry = ssc.get(sscSize - 1);
                int min = sscLastEntry.fe;
                ae.fe = min + 1;
                if (sscSize == k){
                    sscLastEntry.e = ae.e;
                    sscLastEntry.fe = ae.fe;
                } else {
                    ssc.add(ae);
                }
            }
        }
        ssc.sort((o1, o2) -> o1.fe < o2.fe ? 1 : (o1.fe == o2.fe ? 0 : -1));
    }

    private static int invokeSSCT(ArrayEntry [] ssct, Hashtable<Integer, Integer> eToIdx,
                                   Hashtable<Integer, Integer> feToCount, Hashtable<Integer, int[]> feToRange, int elementCount, int n, int k) {

        ArrayEntry ae;
        if (eToIdx.containsKey(n)){
            int idx = eToIdx.get(n);
            ae = ssct[idx];
            int oldFe = ae.fe;
            ae.fe += 1;

            if (idx == k-1){ // changing the last element
                feToCount.put(oldFe, feToCount.get(oldFe) - 1);
                feToCount.put(ae.fe, feToCount.containsKey(ae.fe) ? (feToCount.get(ae.fe)+1) : 1);

                int c = feToCount.get(oldFe);
                if (c > 0){
                    ArrayEntry tmp = ssct[idx - c];
                    ssct[idx - c] = ae;
                    ssct[idx] = tmp;
                    eToIdx.put(tmp.e, idx);
                    eToIdx.put(ae.e, idx - c);

                    if (feToRange.containsKey(ae.fe)){
                        int [] arr = feToRange.get(ae.fe);
                        arr[1] += 1;
                    } else {
                        feToRange.put(ae.fe, new int []{idx-c, idx-c});
                    }
                    int [] oldRange = feToRange.get(oldFe);
                    oldRange[0] += 1;
                    oldRange[1] += 1;
                } else {
                    if (feToRange.containsKey(ae.fe)){
                        int [] arr = feToRange.get(ae.fe);
                        arr[1] += 1;
                    } else {
                        feToRange.put(ae.fe, new int []{idx, idx});
                    }
                    feToCount.remove(oldFe);
                    feToRange.remove(oldFe);
                }
            } else { // changing the first or a middle element
                int [] oldRange = feToRange.get(oldFe);
                feToCount.put(oldFe, feToCount.get(oldFe) - 1);
                feToCount.put(ae.fe, feToCount.containsKey(ae.fe) ? (feToCount.get(ae.fe)+1) : 1);
                int c = feToCount.get(oldFe);

                if (c > 0){
                    if (idx == oldRange[0]){
                        oldRange[0] += 1;
                        if (idx - 1 >= 0 && ssct[idx-1].fe == ae.fe){
                            int [] arr = feToRange.get(ae.fe);
                            arr[1] +=1;
                        } else {
                            // definitely new fe
                            feToRange.put(ae.fe, new int[]{idx,idx});
                        }
                    }  else {
                        int swapWithIdx = oldRange[0];
                        ArrayEntry tmp = ssct[swapWithIdx];
                        ssct[swapWithIdx] = ae;
                        ssct[idx] = tmp;
                        eToIdx.put(tmp.e, idx);
                        eToIdx.put(ae.e, swapWithIdx);

                        oldRange[0] += 1;
                        if (swapWithIdx - 1 >= 0 && ssct[swapWithIdx-1].fe == ae.fe){
                            int [] arr = feToRange.get(ae.fe);
                            arr[1] +=1;
                        } else {
                            // definitely new fe
                            feToRange.put(ae.fe, new int[]{swapWithIdx,swapWithIdx});
                        }
                    }
                } else {
                    if (idx - 1 >= 0 && ssct[idx-1].fe == ae.fe){
                        int [] arr = feToRange.get(ae.fe);
                        arr[1] +=1;
                    } else {
                        // definitely new fe
                        feToRange.put(ae.fe, new int[]{idx,idx});
                    }
                    feToCount.remove(oldFe);
                    feToRange.remove(oldFe);
                }
            }
        } else {
            if (elementCount == k){ // array full
                --elementCount;
                ArrayEntry minEntry = ssct[elementCount];
                eToIdx.remove(minEntry.e);
                ae = new ArrayEntry(n, minEntry.fe+1);
                ssct[elementCount] = ae;
                feToCount.put(minEntry.fe, feToCount.get(minEntry.fe) - 1);
                feToCount.put(ae.fe, feToCount.containsKey(ae.fe) ? (feToCount.get(ae.fe)+1) : 1);

                int c = feToCount.get(minEntry.fe);
                if (c > 0){
                    ArrayEntry tmp = ssct[elementCount - c];
                    ssct[elementCount - c] = ae;
                    ssct[elementCount] = tmp;
                    eToIdx.put(tmp.e, elementCount);
                    eToIdx.put(ae.e, elementCount - c);
                    if (feToRange.containsKey(ae.fe)){
                        int [] arr = feToRange.get(ae.fe);
                        arr[1] += 1;
                    } else {
                        feToRange.put(ae.fe, new int []{elementCount-c, elementCount-c});
                    }
                    int [] arr = feToRange.get(minEntry.fe);
                    arr[0] += 1;
                    arr[1] += 1;
                } else {
                    eToIdx.put(ae.e, elementCount);
                    if (feToRange.containsKey(ae.fe)){
                        int [] arr = feToRange.get(ae.fe);
                        arr[1] += 1;
                    } else {
                        feToRange.put(ae.fe, new int []{elementCount, elementCount});
                    }
                    feToCount.remove(minEntry.fe);
                    feToRange.remove(minEntry.fe);
                }
            } else { // array NOT full
                if (elementCount == 0){
                    ae = new ArrayEntry(n,1);
                    ssct[0] = ae;
                    eToIdx.put(n,0);
                    feToCount.put(ae.fe,1);
                    feToRange.put(ae.fe, new int[]{0,0});
                } else {
                    ArrayEntry minEntry = ssct[elementCount - 1];
                    ae = new ArrayEntry(n, minEntry.fe+1);
                    ssct[elementCount] = ae;
                    feToCount.put(ae.fe, feToCount.containsKey(ae.fe) ? (feToCount.get(ae.fe)+1) : 1);
                    int c = feToCount.get(minEntry.fe);
                    ArrayEntry tmp = ssct[elementCount - c];
                    ssct[elementCount - c] = ae;
                    ssct[elementCount] = tmp;
                    eToIdx.put(tmp.e, elementCount);
                    eToIdx.put(ae.e, elementCount - c);
                    if (feToRange.containsKey(ae.fe)){
                        int [] arr = feToRange.get(ae.fe);
                        arr[1] += 1;
                    } else {
                        feToRange.put(ae.fe, new int[]{elementCount-c, elementCount-c});
                    }
                    int [] arr = feToRange.get(minEntry.fe);
                    arr[0] += 1;
                    arr[1] += 1;
                }
            }
            ++elementCount;
        }
        return elementCount;
    }


}

class ArrayEntry{
    public int e;
    public int fe;

    ArrayEntry() {
    }

    ArrayEntry(int e) {
        this.e = e;
    }

    ArrayEntry(int e, int fe) {
        this.e = e;
        this.fe = fe;
    }

    @Override
    public String toString() {
        return "{" +
                "e=" + e +
                ", fe=" + fe +
                '}';
    }

    //    @Override
//    public String toString() {
//        return String.valueOf(e);
//    }

    @Override
    public boolean equals(Object obj) {
        return ((ArrayEntry)(obj)).e == e;
    }
}
