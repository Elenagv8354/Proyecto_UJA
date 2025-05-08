package com.example.walletssi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMenu1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMenu1 extends Fragment implements View.OnClickListener {
    private CardView cardViewCredencial1;
    private CardView cardViewCredencial2;
    private ImageView imageViewOptionsCredencial1;
    private ImageView imageViewOptionsCredencial2;
    private ImageView imageViewBuscar;
    private NavController navController;
    private CardView selectedCardView = null; // Para rastrear la CardView seleccionada

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FragmentMenu1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMenu1.
     */
    public static FragmentMenu1 newInstance(String param1, String param2) {
        FragmentMenu1 fragment = new FragmentMenu1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        cardViewCredencial1 = view.findViewById(R.id.cardViewCredencial1);
        cardViewCredencial2 = view.findViewById(R.id.cardViewCredencial2);
        imageViewOptionsCredencial1 = view.findViewById(R.id.imageViewOptionsCredencial1);
        imageViewOptionsCredencial2 = view.findViewById(R.id.imageViewOptionsCredencial2);
        imageViewBuscar = view.findViewById(R.id.imageViewBuscar);

        imageViewOptionsCredencial1.setOnClickListener(this::showPopupMenu);
        imageViewOptionsCredencial2.setOnClickListener(this::showPopupMenu);

        cardViewCredencial1.setOnClickListener(this);
        cardViewCredencial2.setOnClickListener(this);

        imageViewBuscar.setOnClickListener(v -> mostrarTodasCredenciales()); // Establecer OnClickListener

        Button botonAñadir = view.findViewById(R.id.boton_menu_1_add); // ID botón "añadir"
        if (botonAñadir != null) {
            botonAñadir.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragmentMenu1_to_fragmentScan));
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);

        // Añadir elementos al menú directamente en el código
        popupMenu.getMenu().add(0, 1, 0, "Eliminar"); // groupId, itemId, order, title
        popupMenu.getMenu().add(0, 2, 0, "Ocultar");

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == 1) { // ID para "Eliminar"
                // Implementa la lógica para eliminar la CardView asociada
                if (view.getId() == R.id.imageViewOptionsCredencial1) {
                    eliminarCredencial(cardViewCredencial1);
                } else if (view.getId() == R.id.imageViewOptionsCredencial2) {
                    eliminarCredencial(cardViewCredencial2);
                }
                return true;
            } else if (itemId == 2) { // ID para "Ocultar"
                // Implementa la lógica para ocultar la CardView asociada
                if (view.getId() == R.id.imageViewOptionsCredencial1) {
                    ocultarCredencial(cardViewCredencial1);
                } else if (view.getId() == R.id.imageViewOptionsCredencial2) {
                    ocultarCredencial(cardViewCredencial2);
                }
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void eliminarCredencial(CardView cardView) {
        if (cardView != null && cardView.getParent() instanceof ViewGroup) {
            ((ViewGroup) cardView.getParent()).removeView(cardView);
            Toast.makeText(requireContext(), "Credencial eliminada", Toast.LENGTH_SHORT).show();
        }
    }

    private void ocultarCredencial(CardView cardView) {
        if (cardView != null) {
            cardView.setVisibility(View.GONE);
            Toast.makeText(requireContext(), "Credencial oculta", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarTodasCredenciales() {
        // Hacer visible la CardView 1
        if (cardViewCredencial1 != null) {
            cardViewCredencial1.setVisibility(View.VISIBLE);
        }

        // Hacer visible la CardView 2
        if (cardViewCredencial2 != null) {
            cardViewCredencial2.setVisibility(View.VISIBLE);
        }

        // ... Haz lo mismo para cualquier otra CardView de credencial que tengas ...

        Toast.makeText(requireContext(), "Mostrando todas las credenciales", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        // Deseleccionar la CardView previamente seleccionada (si existe)
        if (selectedCardView != null) {
            resetCardViewBackground(selectedCardView);
        }

        // Seleccionar la CardView actual
        selectedCardView = (CardView) v;
        setCardViewBackgroundSelected(selectedCardView);

        // Navegar a FragmentCredencial (puedes pasar argumentos si es necesario)
        navController.navigate(R.id.action_menu1_to_credencial);
    }

    private void setCardViewBackgroundSelected(CardView cardView) {
        // Cambia el color de fondo o algún otro atributo visual para indicar selección
        cardView.setCardBackgroundColor(getResources().getColor(R.color.colorCredencial, requireContext().getTheme()));
        // Se puede cambiar la elevación, el borde, etc.
    }

    private void resetCardViewBackground(CardView cardView) {
        // Restaura el color de fondo original u otros atributos
        cardView.setCardBackgroundColor(getResources().getColor(android.R.color.white, requireContext().getTheme())); // Ejemplo de color blanco
        // Restaura otros atributos a su estado original
    }
}
