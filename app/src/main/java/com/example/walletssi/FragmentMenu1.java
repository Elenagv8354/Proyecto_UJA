package com.example.walletssi;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMenu1#} factory method to
 * create an instance of this fragment.
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

    private List<Credencial> listaCredenciales = new ArrayList<>();

    public FragmentMenu1() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        imageViewUsuario = view.findViewById(R.id.imageViewUsuario);
        cardViewCredencial1 = view.findViewById(R.id.CV1);
        cardViewCredencial2 = view.findViewById(R.id.CV2);
        imageViewOptionsCredencial1 = view.findViewById(R.id.imageViewOptionsCredencial1);
        imageViewOptionsCredencial2 = view.findViewById(R.id.imageViewOptionsCredencial2);
        imageViewBuscar = view.findViewById(R.id.imageViewBuscar);

        imageViewUsuario.setOnClickListener(v -> mostrarPopupMenuUsuario(v));
        imageViewOptionsCredencial1.setOnClickListener(this::showPopupMenuCredencial);
        imageViewOptionsCredencial2.setOnClickListener(this::showPopupMenuCredencial);
        cardViewCredencial1.setOnClickListener(this);
        cardViewCredencial2.setOnClickListener(this);
        imageViewBuscar.setOnClickListener(this::mostrarPopupListaCredenciales);

        Button botonAñadir = view.findViewById(R.id.boton_menu_1_add);
        if (botonAñadir != null) {
            botonAñadir.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragmentMenu1_to_fragmentScan));
        }

        // Inicialización de la lista de credenciales (asegúrate de que los nombres coincidan con tus CardViews)
        listaCredenciales.add(new Credencial("CV1", false));
        listaCredenciales.add(new Credencial("CV2", false));
        // ... añade más credenciales según tus CardViews ...
    }

    private void mostrarPopupMenuUsuario(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_perfil, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            // ... (tu lógica del menú de usuario existente) ...
            if (itemId == R.id.action_cerrar_sesion) {
                cerrarSesion();
                return true;
            }
            return true;
        });
        popupMenu.show();
    }

    private void cerrarSesion() {
        // Aquí puedes añadir cualquier lógica adicional de limpieza o guardado de estado
        // antes de cerrar la sesión.

        // Crea un Intent para iniciar la Activity de inicio de sesión
        Intent intent = new Intent(requireContext(), ActivityBienvenida.class);

        // Opcional: Puedes añadir flags al Intent si necesitas limpiar la pila de actividades y evitar volver atrás
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Inicia la Activity de inicio de sesión
        startActivity(intent);

        // Finaliza la actividad actual (FragmentMenu1 Activity)
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

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
        CredencialAdapter popupAdapter = new CredencialAdapter(listaCredenciales, this);
        recyclerViewPopup.setAdapter(popupAdapter);

        if (listaCredenciales.isEmpty()) {
            recyclerViewPopup.setVisibility(View.GONE);
            textViewPopupListaVacia.setVisibility(View.VISIBLE);
        } else {
            recyclerViewPopup.setVisibility(View.VISIBLE);
            textViewPopupListaVacia.setVisibility(View.GONE);
        }

        popupWindow.showAsDropDown(anchorView, 0, 0, Gravity.END);
    }

    @Override
    public void onRestaurarClick(Credencial credencial) {
        mostrarDialogoRestaurar(credencial);
    }

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
                    // Notificar al adaptador del popup que los datos han cambiado
                    // Esto es importante para que el botón "Mostrar" desaparezca si la credencial ya no está oculta
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

    private void mostrarTodasCredenciales() {
        if (cardViewCredencial1 != null) {
            cardViewCredencial1.setVisibility(View.VISIBLE);
        }
        if (cardViewCredencial2 != null) {
            cardViewCredencial2.setVisibility(View.VISIBLE);
        }
        // También deberías actualizar el estado 'oculta' en la lista
        for (Credencial c : listaCredenciales) {
            c.setOculta(false);
        }
        mostrarPopupListaCredenciales(imageViewBuscar); // Refrescar la lista en el popup
        Toast.makeText(requireContext(), "Mostrando todas las credenciales", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (selectedCardView != null) {
            resetCardViewBackground(selectedCardView);
        }
        selectedCardView = (CardView) v;
        setCardViewBackgroundSelected(selectedCardView);
        navController.navigate(R.id.action_menu1_to_credencial);
    }

    private void setCardViewBackgroundSelected(CardView cardView) {
        cardView.setCardBackgroundColor(getResources().getColor(R.color.colorCredencial, requireContext().getTheme()));
    }

    private void resetCardViewBackground(CardView cardView) {
        cardView.setCardBackgroundColor(getResources().getColor(android.R.color.white, requireContext().getTheme()));
    }
}
