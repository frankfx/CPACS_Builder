package de.utils.math.vector;

public class Vector {
	
	// res = a cross b;
	public static void crossProduct(float a[], float b[], float res[]) {
		res[0] = a[1] * b[2] - b[1] * a[2];
		res[1] = a[2] * b[0] - b[2] * a[0];
		res[2] = a[0] * b[1] - b[0] * a[1];
	}

	// Normalize a vec3
	public static void normalize(float a[]) {
		float mag = (float) Math.sqrt(a[0] * a[0] + a[1] * a[1] + a[2] * a[2]);
		a[0] /= mag;
		a[1] /= mag;
		a[2] /= mag;
	}
	
	public static void show(float a[]){
		System.out.println("<" + a[0] + ", " + a[1] + ", " + a[2] + ">");
	}
	
	public static void main(String[] args) {
		float[] vec1 = new float[]{0.1f, 2f, 1f};
		
		show(vec1);
		normalize(vec1);
		show(vec1);
		
		float[] vec2 = new float[]{1f, 1f, 1f};
		float[] vec3 = new float[]{0.1f, 1f, 1f};
		float[] vec_res = new float[]{0f, 0f, 0f};
		
		show(vec_res);
		crossProduct(vec2, vec3, vec_res);
		show(vec_res);
	}
}
