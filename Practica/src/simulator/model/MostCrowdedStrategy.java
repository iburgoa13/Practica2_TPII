package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {


    protected int _timeSlot;
    public MostCrowdedStrategy(int t){
        _timeSlot = t;
    }

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {

	       if(roads.isEmpty()) return -1;
	       if(currGreen == -1){
	           int max = 0;
	           int ind = 0;
	           for(int i = 0; i < qs.size();i++){
	               if(qs.get(i).size() > max){
	                   max = qs.get(i).size();
	                   ind = i;
	               }
	           }
	           return ind;
	       }
	       if(currTime - lastSwitchingTime < _timeSlot) return currGreen;

	       //busqueda circular
		int i = (currGreen+1)%roads.size();
		int max = 0;
		int ind = i;

		int total = 0;
		while(total < roads.size()){
			if(i==roads.size()){
				i = i%roads.size();
			}
			if(qs.get(i).size() > max){
				max = qs.get(i).size();
				ind = i;

			}
			i++;
			total++;
		}
	     /*  while(i < roads.size()){
	           if(qs.get(i).size() > max){
	               max = qs.get(i).size();
	               ind = i;
	           }
	           i++;
	       }*/
	       return ind;

	}

}

