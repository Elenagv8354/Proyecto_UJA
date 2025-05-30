package com.example.walletssi;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import androidx.navigation.Navigation;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;

import android.widget.PopupMenu;
import android.widget.Toast;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import android.view.Gravity;

import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * FragmentMenu1 es el fragmento principal del menú de la aplicación,
 * accesible después de la autenticación. Muestra las credenciales del usuario,
 * proporciona opciones para gestionarlas (eliminar, ocultar, mostrar)
 * y permite navegar a otras secciones como la generación de QR o el perfil del usuario.
 * También integra una lista de "actividad reciente" (aunque no se use en el código mostrado).
 */

public class FragmentMenu1 extends Fragment implements View.OnClickListener, CredencialAdapter.OnRestaurarClickListener {
    private CardView cardViewCredencial1;
    private CardView cardViewCredencial2;
    private ImageView imageViewOptionsCredencial1;
    private ImageView imageViewOptionsCredencial2;
    private ImageView imageViewBuscar;
    private ImageView imageViewUsuario;
    private NavController navController;
    private CardView selectedCardView = null;
    private CredencialAdapter popupAdapter;

    private RecyclerView recyclerViewActividad;
    private List<String> listaActividadReciente = new ArrayList<>();
    private ArrayAdapter<String> adapterActividadReciente;

    private List<Credencial> listaCredenciales = new ArrayList<>();

    public FragmentMenu1() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicialización de la lista de credenciales
        if (listaCredenciales.isEmpty()) {
            listaCredenciales.add(new Credencial("CV1", false));
            listaCredenciales.add(new Credencial("CV2", false));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Inicializa el NavController para gestionar la navegación.
        navController = Navigation.findNavController(view);

        // Inicializa las referencias a los componentes de la UI y sus listeners.
        imageViewUsuario = view.findViewById(R.id.imageViewUsuario);
        cardViewCredencial1 = view.findViewById(R.id.CV1);
        cardViewCredencial2 = view.findViewById(R.id.CV2);
        imageViewOptionsCredencial1 = view.findViewById(R.id.imageViewOptionsCredencial1);
        imageViewOptionsCredencial2 = view.findViewById(R.id.imageViewOptionsCredencial2);
        imageViewBuscar = view.findViewById(R.id.imageViewBuscar);

        // Configura los listeners para los botones y CardViews.
        imageViewUsuario.setOnClickListener(v -> mostrarPopupMenuUsuario(v));
        imageViewOptionsCredencial1.setOnClickListener(this::showPopupMenuCredencial);
        imageViewOptionsCredencial2.setOnClickListener(this::showPopupMenuCredencial);
        cardViewCredencial1.setOnClickListener(this);
        cardViewCredencial2.setOnClickListener(this);
        imageViewBuscar.setOnClickListener(this::mostrarPopupListaCredenciales);

        // Configura el botón para añadir nuevas credenciales navegando a la pantalla de escaneo.
        Button botonAñadir = view.findViewById(R.id.boton_menu_1_add);
        if (botonAñadir != null) {
            botonAñadir.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragmentMenu1_to_fragmentScan));
        }
    }

    // Muestra un menú emergente para el usuario, con opciones como cerrar sesión.
    private void mostrarPopupMenuUsuario(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_perfil, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_cerrar_sesion) {
                cerrarSesion();
                return true;
            }
            return true;
        });
        popupMenu.show();
    }

    //Cierra la sesión del usuario, redirigiéndolo a la pantalla de bienvenida y finalizando la actividad actual
    private void cerrarSesion() {
        // Intent para iniciar la Activity de inicio de sesión
        Intent intent = new Intent(requireContext(), ActivityBienvenida.class);

        // Añadir flags al Intent para limpiar la pila de actividades y evitar volver atrás
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Inicia la Activity de inicio de sesión
        startActivity(intent);

        // Finaliza la actividad actual (FragmentMenu1 Activity)
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    //Muestra un menú emergente con opciones para gestionar una credencial específica (eliminar u ocultar).
    private void showPopupMenuCredencial(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.getMenu().add(0, 1, 0, "Eliminar");
        popupMenu.getMenu().add(0, 2, 0, "Ocultar");
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == 1) { // Eliminar
                mostrarDialogoConfirmacion("¿Seguro que quieres eliminar esta credencial?", () -> eliminarCredencial(view));
                return true;
            } else if (itemId == 2) { // Ocultar
                ocultarCredencial(view);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    //Muestra un popup flotante con una lista de todas las credenciales, permitiendo restaurar las que están ocultas.
    private void mostrarPopupListaCredenciales(View anchorView) {
        LayoutInflater inflater = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_credenciales, null);

        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));

        RecyclerView recyclerViewPopup = popupView.findViewById(R.id.recyclerViewPopupCredenciales);
        TextView textViewPopupListaVacia = popupView.findViewById(R.id.textViewPopupListaVacia);

        recyclerViewPopup.setLayoutManager(new LinearLayoutManager(requireContext()));
        popupAdapter = new CredencialAdapter(listaCredenciales, this);
        recyclerViewPopup.setAdapter(popupAdapter);

        actualizarVisibilidadListaPopup(recyclerViewPopup, textViewPopupListaVacia);

        popupWindow.showAsDropDown(anchorView, 0, 0, Gravity.END);
    }

    // Actualiza la visibilidad del RecyclerView y el mensaje de lista vacía en el popup.
    // Notifica al adaptador sobre posibles cambios en los datos.
    private void actualizarVisibilidadListaPopup(RecyclerView recyclerView, TextView textViewListaVacia) {
        List<Credencial> credencialesVisiblesEnPopup = new ArrayList<>();
        for (Credencial credencial : listaCredenciales) {
            // Siempre mostrar todas las credenciales en el popup, pero el adaptador controlará el botón "Mostrar"
            credencialesVisiblesEnPopup.add(credencial);
        }

        if (credencialesVisiblesEnPopup.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            textViewListaVacia.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            textViewListaVacia.setVisibility(View.GONE);
            if (popupAdapter != null) {
                popupAdapter.notifyDataSetChanged(); // Notifica al adaptador que los datos pueden haber cambiado
            }
        }
    }

    @Override
    public void onRestaurarClick(Credencial credencial) {
        mostrarDialogoRestaurar(credencial);
    }

    // Muestra un diálogo de confirmación para restaurar una credencial oculta.
    private void mostrarDialogoRestaurar(Credencial credencial) {
        new AlertDialog.Builder(requireContext())
                .setMessage("¿Quieres mostrar la credencial '" + credencial.getNombre() + "'?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    credencial.setOculta(false);
                    // Actualizar la visibilidad de la CardView correspondiente
                    if (credencial.getNombre().equals("CV1")) {
                        cardViewCredencial1.setVisibility(View.VISIBLE);
                    } else if (credencial.getNombre().equals("CV2")) {
                        cardViewCredencial2.setVisibility(View.VISIBLE);
                    }
                    // Notifica al adaptador del popup que los datos han cambiado
                    mostrarPopupListaCredenciales(imageViewBuscar); // Reabrir el popup para refrescar la lista
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void mostrarDialogoConfirmacion(String mensaje, Runnable accionConfirmar) {
        new AlertDialog.Builder(requireContext())
                .setMessage(mensaje)
                .setPositiveButton("Aceptar", (dialog, which) -> accionConfirmar.run())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Elimina una credencial de la interfaz de usuario y de la lista de datos.
    private void eliminarCredencial(View viewOpciones) {
        CardView cardViewToRemove = null;
        String nombreCredencialToRemove = "";
        if (viewOpciones.getId() == R.id.imageViewOptionsCredencial1) {
            cardViewToRemove = cardViewCredencial1;
            nombreCredencialToRemove = "CV1";
        } else if (viewOpciones.getId() == R.id.imageViewOptionsCredencial2) {
            cardViewToRemove = cardViewCredencial2;
            nombreCredencialToRemove = "CV2";
        }

        if (cardViewToRemove != null && cardViewToRemove.getParent() instanceof ViewGroup) {
            ((ViewGroup) cardViewToRemove.getParent()).removeView(cardViewToRemove);
            // Eliminar la credencial de la lista
            Iterator<Credencial> iterator = listaCredenciales.iterator();
            while (iterator.hasNext()) {
                Credencial credencial = iterator.next();
                if (credencial.getNombre().equals(nombreCredencialToRemove)) {
                    iterator.remove();
                    break;
                }
            }
            Toast.makeText(requireContext(), "Credencial eliminada", Toast.LENGTH_SHORT).show();
            mostrarPopupListaCredenciales(imageViewBuscar); // Refrescar la lista en el popup
        }
    }

    // Oculta una credencial de la interfaz de usuario y actualiza su estado en la lista de datos.
    private void ocultarCredencial(View viewOpciones) {
        CardView cardViewToHide = null;
        String nombreCredencialToHide = "";
        if (viewOpciones.getId() == R.id.imageViewOptionsCredencial1) {
            cardViewToHide = cardViewCredencial1;
            nombreCredencialToHide = "CV1";
        } else if (viewOpciones.getId() == R.id.imageViewOptionsCredencial2) {
            cardViewToHide = cardViewCredencial2;
            nombreCredencialToHide = "CV2";
        }

        if (cardViewToHide != null) {
            cardViewToHide.setVisibility(View.GONE);
            // Actualizar el estado 'oculta' en la lista
            for (Credencial c : listaCredenciales) {
                if (c.getNombre().equals(nombreCredencialToHide)) {
                    c.setOculta(true);
                    break;
                }
            }
            Toast.makeText(requireContext(), "Credencial oculta", Toast.LENGTH_SHORT).show();
            mostrarPopupListaCredenciales(imageViewBuscar); // Refrescar la lista en el popup
        }
    }

    //Muestra todas las credenciales ocultas en la interfaz de usuario y actualiza su estado en la lista de datos.
    private void mostrarTodasCredenciales() {
        if (cardViewCredencial1 != null) {
            cardViewCredencial1.setVisibility(View.VISIBLE);
        }
        if (cardViewCredencial2 != null) {
            cardViewCredencial2.setVisibility(View.VISIBLE);
        }

        for (Credencial c : listaCredenciales) {
            c.setOculta(false);
        }
        mostrarPopupListaCredenciales(imageViewBuscar); // Refrescar la lista en el popup
        Toast.makeText(requireContext(), "Mostrando todas las credenciales", Toast.LENGTH_SHORT).show();
    }

    // Listener genérico para los clics en las CardViews de las credenciales.
    // Maneja el resaltado de la CardView seleccionada y la navegación al FragmentCredencial.
    @Override
    public void onClick(View v) {
        if (selectedCardView != null) {
            resetCardViewBackground(selectedCardView);
        }
        selectedCardView = (CardView) v;
        setCardViewBackgroundSelected(selectedCardView);
        navController.navigate(R.id.action_menu1_to_credencial);
    }

    // Establece el color de fondo de una CardView para indicar que está seleccionada.
    private void setCardViewBackgroundSelected(CardView cardView) {
        cardView.setCardBackgroundColor(getResources().getColor(R.color.colorCredencial, requireContext().getTheme()));
    }

    // Restablece el color de fondo de una CardView a su estado original (no seleccionada).
    private void resetCardViewBackground(CardView cardView) {
        cardView.setCardBackgroundColor(getResources().getColor(android.R.color.white, requireContext().getTheme()));
    }
}
