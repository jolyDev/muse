//package org.andresoviedo.app.model3D.arcorehelpers;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageButton;
//
//import com.app.animals3D.R;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//
//public class AnimalsAdapter extends BaseAdapter {
//    private final Context context;
//    private final LayoutInflater inflater;
//    private final ArrayList<Animal> items;
//
//    public AnimalsAdapter(Context context) {
//        this.context = context;
//        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        items = new ArrayList<>(
//                Arrays.asList(
//                        new Animal("Tiger", "https://storage.googleapis.com/ar-answers-in-search-models/static/Tiger/model.glb")
//                )
//        );
//        // Shuffle element in order to have a different order every time.
//        Collections.shuffle(items);
//
//    }
//
//    @Override
//    public int getCount() {
//        return items.size();
//    }
//
//    @Override
//    public Animal getItem(int position) {
//        return items.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return getItem(position).hashCode();
//    }
//
//    @Override
//    public View getView(int position, View view, ViewGroup viewGroup) {
//
//        if (view == null) {
//            view = inflater.inflate(R.layout.grid_item, viewGroup, false);
//        }
//
//        final Animal animal = getItem(position);
//        ImageButton buttonAnimal = view.findViewById(R.id.imageButton_animal);
//        buttonAnimal.setImageResource(animal.getIdDrawable());
//        buttonAnimal.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Log.i(context.getClass().getSimpleName(), "Open 3D " + animal.getName());
//                ArCoreHelper.showArObject(
//                        context,
//                        animal.getSource3D(),
//                        animal.getName());
//            }
//        });
//
//        return view;
//    }
//
//}
