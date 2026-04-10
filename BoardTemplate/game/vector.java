package game;

public class vector {

	
	
	public  double xDimension;
	public double yDimension;
	public double zDimension;
	public double wDimension;
	public double w1Dimension;
	public double w2Dimension;
	public double that_Little_variable_Called_t;

	vector(int x, int y, int z, int w , int w1 ,int w2,double t){
		xDimension = x;
		yDimension = y;
		zDimension = z;
		wDimension = w;
		w1Dimension = w1;
		w2Dimension = w2;
		that_Little_variable_Called_t=t;
	}
	
		
	
	
	public vector(double x, double y, double z, double w, double w1, double w2, double t) {
		xDimension = x;
		yDimension = y;
		zDimension = z;
		wDimension = w;
		w1Dimension = w1;
		w2Dimension = w2;
		that_Little_variable_Called_t=t;
	}

	public vector(double x, double y, double z, double w) {
		xDimension = x;
		yDimension = y;
		zDimension = z;
		wDimension = w;
        that_Little_variable_Called_t=0;
        	w1Dimension=0;
        	w2Dimension=0;
        	
	}






	public double getxDimension() {
		return xDimension;
	}
	public void setxDimension(int xDimension) {
		this.xDimension = xDimension;
	}
	public double getyDimension() {
		return yDimension;
	}
	public void setyDimension(int yDimension) {
		this.yDimension = yDimension;
	}
	public double getzDimension() {
		return zDimension;
	}
	public void setzDimension(int zDimension) {
		this.zDimension = zDimension;
	}
	public double getwDimension() {
		return wDimension;
	}
	public void setwDimension(int wDimension) {
		this.wDimension = wDimension;
	}
	
	
	public double getw1Dimension() {
		return w1Dimension;
	}
	public void setw1Dimension(int w1Dimension) {
		this.w1Dimension = w1Dimension;
	}
	
	
	public double getw2Dimension() {
		return w2Dimension;
	}
	public void setw2Dimension(int w2Dimension) {
		this.w2Dimension = w2Dimension;
	}
	
	public double getThat_Little_variable_Called_t() {
		return that_Little_variable_Called_t;
	}
	public void setThat_Little_variable_Called_t(int that_Little_variable_Called_t) {
		this.that_Little_variable_Called_t = that_Little_variable_Called_t;
	}




	public void println() {
		System.out.println("[x][y][z][w][w1][w2][t] = ["+xDimension+"]["+yDimension+"]["+zDimension+"]["+wDimension+"]["+w1Dimension+"]["+w2Dimension+"]["+that_Little_variable_Called_t+"]");
	}




	public void println(String dimension_IDENTIFIER_iguess) {
		System.out.println(dimension_IDENTIFIER_iguess+"[x][y][z][w][w1][w2][t] = ["+xDimension+"]["+yDimension+"]["+zDimension+"]["+wDimension+"]["+w1Dimension+"]["+w2Dimension+"]["+that_Little_variable_Called_t+"]");

	}




	public double[] toArray() {
		double[] temp=new double[7];
		temp[0]=xDimension;
		temp[1]=yDimension;
		temp[2]=zDimension;
		temp[3]=wDimension;
		temp[4]=w1Dimension;
		temp[5]=w2Dimension;
		temp[6]=that_Little_variable_Called_t;
		return temp;
	}
	
}
