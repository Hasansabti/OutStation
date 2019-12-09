package com.example.himanshupalve.carrental;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.himanshupalve.carrental.Utils.IPUtils;
import com.example.himanshupalve.carrental.models.Car;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CarListActivity extends AppCompatActivity {
    RecyclerView carList;
    MyRVAdapter adapter;
    ArrayList<Car> carDetails;
    String address;
    String city;
    String pincode;
    View buttonRv;
    TextView paddingTV;
    Animation a;
    JSONObject searchQuery;
    Button proceed;

    // [START declare_database_ref]
    private FirebaseDatabase mydatabase;
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private int selectedPos=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        carDetails=new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
        FirebaseApp.initializeApp(this);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        mydatabase = FirebaseDatabase.getInstance();

        mDatabase.child("cars").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (carDetails.size() > 0)
                    carDetails.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Car car = ds.getValue(Car.class);
                    carDetails.add(car);


                    Log.d("car", car.getName());
                }
                if (adapter != null)
                    adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });








        buttonRv= findViewById(R.id.button_rv);
        paddingTV= findViewById(R.id.paddingTV);
        carList=findViewById(R.id.rec_view);
        proceed=findViewById(R.id.btnproceed);

        a= AnimationUtils.loadAnimation(this,R.anim.button_popup);
        buttonRv.setAnimation(a);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        adapter=new MyRVAdapter(this);
        carList.setLayoutManager(layoutManager);
        carList.setAdapter(adapter);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Car o= carDetails.get(selectedPos);

    //                TODO: add intent and searchQuery for your activity;
                    if(selectedPos>=0){
                        startActivity(new Intent(CarListActivity.this,CarFinallizeActivity.class)
                        .putExtra("searchQuery","query")
                        .putExtra("selectedCar",o.getName()));
                    }

            }
        });

       // if(savedInstanceState==null)
           // getActualCars(getIntent().getExtras());
//        else
//            getActualCars(savedInstanceState);
        adapter.notifyDataSetChanged();
        JSONObject searchQuery=new JSONObject();
        try {
            searchQuery.put("address",address);
            searchQuery.put("pincode", pincode);
            searchQuery.put("startDate", "startDate ");
            searchQuery.put("endDate", "endDate");
            searchQuery.put("city", city);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        RequestQueue queue = Volley.newRequestQueue(CarListActivity.this);
//        PostRequestHandler handler=new PostRequestHandler("notes/getCars",new ResponseObject() {
//            @Override
//            public void onResponse(JSONObject res) {
//                Log.d("cars", "onResponse: " + res.toString());
//                try {
//                    carDetails =res.getJSONArray("result");
//                    adapter.notifyDataSetChanged();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        StringRequest req =handler.postStringRequest(searchQuery);
//        queue.add(req);
        createDummyCars();
    }

    private void createDummyCars(){
/*
        Car car = new Car("BMW M5", UUID.randomUUID().toString(),4,5);
        Car car2 = new Car("Audi A8", UUID.randomUUID().toString(),4,5);
        Car car3 = new Car("Camry", UUID.randomUUID().toString(),4,5);
        Car car4 = new Car("Corolla", UUID.randomUUID().toString(),4,5);
        Car car5 = new Car("Cerato", UUID.randomUUID().toString(),4,5);
        Car car6 = new Car("Jaguar", UUID.randomUUID().toString(),4,5);

        mDatabase.child("cars").child(car.getUuid()).setValue(car);
        mDatabase.child("cars").child(car2.getUuid()).setValue(car2);
        mDatabase.child("cars").child(car3.getUuid()).setValue(car3);
        mDatabase.child("cars").child(car4.getUuid()).setValue(car4);
        mDatabase.child("cars").child(car5.getUuid()).setValue(car5);
        mDatabase.child("cars").child(car6.getUuid()).setValue(car6);

*/
        // Log.d("Carname",mDatabase.child("cars").);
    }


    class MyRVAdapter extends RecyclerView.Adapter<MyRVAdapter.ViewHolder>{

        Context context;

        public MyRVAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.recview, parent, false);
            return new ViewHolder(listItem);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            holder.itemView.setSelected(false);

                String name=(carDetails.get(position)).getName();
                String seat=""+( carDetails.get(position)).getSeats();
                String rate=""+( carDetails.get(position)).getRating();
                String type=( carDetails.get(position)).getFuel();
                String regNo=( carDetails.get(position)).getUuid();
               // Glide.with(CarListActivity.this)
             //           .load(IPUtils.getCompleteip()+"/getImage?regNo="+regNo)
              //          .into(holder.carImage);


            StorageReference storageRef =
                    FirebaseStorage.getInstance().getReference();
            storageRef.child("images/"+carDetails.get(position).getUuid()).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                             Glide.with(CarListActivity.this)
                                       .load(uri)
                                      .into(holder.carImage);
                        }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });
                int stars=(carDetails.get(position)).getRating();
                holder.carName.setText(name);
                holder.seats.setText(seat);
                holder.rate.setText(rate);
                holder.type.setText(type);
//                holder.rating.setText(stars);
                holder.addStars(stars);
//                holder.addStars(position%5);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.reLativeLayout));
                    Log.d("cars", "onClick:  rvitem");
//                    Toast.makeText(CarListActivity.this,"Car no. "+position+ "Selected",Toast.LENGTH_SHORT).show();
                    if(selectedPos!=holder.getAdapterPosition()) {
                        notifyItemChanged(selectedPos);
                        selectedPos=holder.getAdapterPosition();
                        buttonRv.setVisibility(View.VISIBLE);
                        paddingTV.setVisibility(View.VISIBLE);
                    }
                    else {
                        selectedPos=-1;
                        buttonRv.setVisibility(View.GONE);
                        paddingTV.setVisibility(View.GONE);
                    }
                    holder.select(!holder.itemView.isSelected());
                }
            });
        }

        @Override
        public int getItemCount() {
            return carDetails.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView carImage;
            TextView carName;
            TextView seats;
            TextView rating;
            RelativeLayout relativeLayout;
            TextView rate;
            TextView type;
            LinearLayout ratingsLL;
            private ViewHolder(@NonNull View itemView) {
                super(itemView);
                carName=itemView.findViewById(R.id.name);
                carImage=itemView.findViewById(R.id.car_image);
                seats=itemView.findViewById(R.id.seater);
                rating=itemView.findViewById(R.id.rating);
                type=itemView.findViewById(R.id.type);
                rate=itemView.findViewById(R.id.rate_name);
                relativeLayout=itemView.findViewById(R.id.reLativeLayout);
                ratingsLL=itemView.findViewById(R.id.ratingsLL);
            }
            void select(boolean b){
                itemView.setSelected(b);
            }
            void addStars(int rating){
                ratingsLL.removeAllViews();
                for (int i=0;i<rating;i++){
                    ImageView star=new ImageView(CarListActivity.this);
                    star.setImageResource(R.drawable.ic_star_black_24dp);
                    ratingsLL.addView(star,new LinearLayout.LayoutParams(24,24,1));
                }
            }
        }

    }

}