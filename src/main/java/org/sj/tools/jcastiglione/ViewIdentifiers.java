package org.sj.tools.jcastiglione;

public interface ViewIdentifiers {
	public static final int SEND_BACKGROUND = 1;
	public static final int MSG_AUMENTAR = 2;
	public static final int MSG_DISMINUIR = 3;
	public static final int MSG_CHG_SCROLL = 4;
	public static final int MSG_AJUSTAR_FONDO = 5;
	public static final int MSG_SET_ACCION = 6;
	public static final int MSG_REFRESH_FIGURA = 7;
	public static final int MSG_REPAINT = 8;
	
	/* acciones posibles */
	public static final int AC_NINGUNA = 0;
	public static final int AC_NEW_LINEA = 1;
	public static final int AC_NEW_RECT = 2;
	public static final int AC_NEW_OVALO = 3;
	public static final int AC_NEW_ARCO = 4;
	public static final int AC_NEW_POLI = 5;
	public static final int AC_NEW_IMG = 6;
	public static final int AC_NEW_MALLA = 7;
	public static final int AC_NEW_TEXT = 8;
	
	public static final int AC_NEW_VERT = 22;
	public static final int AC_ADD_VERT = 23;
	public static final int AC_EDIT_VERT = 24;
	public static final int AC_DEL_VERT = 25;
	public static final int AC_MOVE_VERT = 26;
	public static final int AC_SELECCIONAR = 30;
	public static final int AC_SELEC_DRAG = 31;
	public static final int AC_SEL_MOVER = 32;
	public static final int AC_BORRAR = 40;
	
	public static final int AC_EDIT_TEXT = 41;


	/* tipos de malla */
	public static final int MALLA_NO = 0;
	public static final int MALLA_CUAD = 1; /* malla cuadrada */
	public static final int MALLA_TRI = 2;  /* malla triangular */

}
