package com.example.shivamvk.mindfulmachine;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class PlaceOrderFragment extends Fragment {

    private TextView tvNotificationText;
    private ImageView ivCloseNotification;
    private RelativeLayout rlNoificationHeader;
    private EditText etLoadingPoint,etTripDestination,etTruckType,etMaterialType, etLoadingDate,etLoadingTime,etRemarks;
    private Button btPlaceOrder;

    private Spinner spPaymentType,spNoOfTrucks;

    private static final String[] spNoOfTrucksList={"No of trucks","No of trucks: 1","No of trucks: 2","No of trucks: 3","No of trucks: 4","No of trucks: 5"};
    private static final String[] spPaymentTypeList = {"Payment Type","Advance To Pay Driver - Cash","Advance To Pay - OHFL","Full advance - Cash"
            ,"Full advance OHFL","Total To Pay - Cash","Total To Pay - Cash, Cheque","Total To Pay OHFL"};

    private int LOADING_POINT_REQUEST_CODE = 1;
    private int TRIP_DESTINATION_REQUEST_CODE = 2;
   // private int hour,minutes,hourOfDay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place_order, null, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Mindful Machine");

        tvNotificationText = view.findViewById(R.id.tv_notification_text);
        ivCloseNotification = view.findViewById(R.id.iv_close_notification);
        rlNoificationHeader = view.findViewById(R.id.rl_notification_header);
        etLoadingPoint = view.findViewById(R.id.et_place_order_loading_point);
        etTripDestination = view.findViewById(R.id.et_place_order_trip_destination);
        etTruckType = view.findViewById(R.id.et_place_order_truck_type);
        etMaterialType = view.findViewById(R.id.et_place_order_material_type);
        etLoadingDate = view.findViewById(R.id.et_place_order_loading_date);

        etLoadingTime = view.findViewById(R.id.et_place_order_loading_time);


        spPaymentType = view.findViewById(R.id.sp_place_order_payment_type);
        spNoOfTrucks = view.findViewById(R.id.sp_place_order_no_of_trucks);

        etRemarks = view.findViewById(R.id.et_place_order_remarks);

        btPlaceOrder = view.findViewById(R.id.bt_place_order);

        if (SharedPrefManager.getInstance(getContext()).isEmailVerified().equals("Yes")){
            rlNoificationHeader.setVisibility(View.GONE);
        }

        ivCloseNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlNoificationHeader.setVisibility(View.GONE);
            }
        });

        etLoadingPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPrefManager.getInstance(getContext()).isNumberVerified().equals("No")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Please verify your email before placing an order!");
                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Fragment fragment=new AccountFragment();
                            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fl_home_activity,fragment);
                            fragmentTransaction.commit();
                        }
                    });
                    builder.show();
                    return;
                }
                try {
                    getActivity().setTitle("Create an order");
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(getActivity());
                    startActivityForResult(intent, LOADING_POINT_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

        etTripDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(getActivity());
                    startActivityForResult(intent, TRIP_DESTINATION_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

        etTruckType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.dialog_truck_type, null, false);
                final LinearLayout llTruckTypeOpen,llTruckTypeContainer,llTruckTypeTrailer;

                llTruckTypeOpen = view1.findViewById(R.id.ll_truck_type_open);
                llTruckTypeContainer = view1.findViewById(R.id.ll_truck_type_container);
                llTruckTypeTrailer = view1.findViewById(R.id.ll_truck_type_trailer);

                builder.setView(view1);
                final AlertDialog dialog = builder.show();

                llTruckTypeOpen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder1=new AlertDialog.Builder(getActivity());
                        View view2 = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_truck_type_open, null, false);
                        final TextView tvDialogTruckTypeOpenOne = view2.findViewById(R.id.tv_dialog_truck_type_open_one);
                        final TextView tvDialogTruckTypeOpenTwo = view2.findViewById(R.id.tv_dialog_truck_type_open_two);
                        final TextView tvDialogTruckTypeOpenThree = view2.findViewById(R.id.tv_dialog_truck_type_open_three);
                        final TextView tvDialogTruckTypeOpenFour = view2.findViewById(R.id.tv_dialog_truck_type_open_four);
                        final TextView tvDialogTruckTypeOpenFive = view2.findViewById(R.id.tv_dialog_truck_type_open_five);
                        final TextView tvDialogTruckTypeOpenSix = view2.findViewById(R.id.tv_dialog_truck_type_open_six);
                        final TextView tvDialogTruckTypeOpenSeven = view2.findViewById(R.id.tv_dialog_truck_type_open_seven);
                        final TextView tvDialogTruckTypeOpenEight = view2.findViewById(R.id.tv_dialog_truck_type_open_eight);
                        builder1.setView(view2);
                        final AlertDialog dialog1 = builder1.show();
                        tvDialogTruckTypeOpenOne.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tvDialogTruckTypeOpenOne.setBackgroundColor(getResources().getColor(R.color.green));
                                etTruckType.setText("Open - "+getString(R.string.truck_type_open_1));
                                dialog.dismiss();
                                dialog1.dismiss();
                                etMaterialType.setVisibility(View.VISIBLE);
                            }
                        });
                        tvDialogTruckTypeOpenTwo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tvDialogTruckTypeOpenTwo.setBackgroundColor(getResources().getColor(R.color.green));
                                etTruckType.setText("Open - "+getString(R.string.truck_type_open_2));
                                dialog.dismiss();
                                dialog1.dismiss();
                                etMaterialType.setVisibility(View.VISIBLE);

                            }
                        });
                        tvDialogTruckTypeOpenThree.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tvDialogTruckTypeOpenThree.setBackgroundColor(getResources().getColor(R.color.green));
                                etTruckType.setText("Open - "+getString(R.string.truck_type_open_3));
                                dialog.dismiss();
                                dialog1.dismiss();
                                etMaterialType.setVisibility(View.VISIBLE);

                            }
                        });
                        tvDialogTruckTypeOpenFour.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tvDialogTruckTypeOpenFour.setBackgroundColor(getResources().getColor(R.color.green));
                                etTruckType.setText("Open - "+getString(R.string.truck_type_open_4));
                                dialog.dismiss();
                                dialog1.dismiss();
                                etMaterialType.setVisibility(View.VISIBLE);

                            }
                        });
                        tvDialogTruckTypeOpenFive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tvDialogTruckTypeOpenFive.setBackgroundColor(getResources().getColor(R.color.green));
                                etTruckType.setText("Open - "+getString(R.string.truck_type_open_5));
                                dialog.dismiss();
                                dialog1.dismiss();
                                etMaterialType.setVisibility(View.VISIBLE);

                            }
                        });
                        tvDialogTruckTypeOpenSix.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tvDialogTruckTypeOpenSix.setBackgroundColor(getResources().getColor(R.color.green));
                                etTruckType.setText("Open - "+getString(R.string.truck_type_open_6));
                                dialog.dismiss();
                                dialog1.dismiss();
                                etMaterialType.setVisibility(View.VISIBLE);
                            }
                        });
                        tvDialogTruckTypeOpenSeven.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tvDialogTruckTypeOpenSeven.setBackgroundColor(getResources().getColor(R.color.green));
                                etTruckType.setText("Open - "+getString(R.string.truck_type_open_7));
                                dialog.dismiss();
                                dialog1.dismiss();
                                etMaterialType.setVisibility(View.VISIBLE);
                            }
                        });
                        tvDialogTruckTypeOpenEight.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tvDialogTruckTypeOpenEight.setBackgroundColor(getResources().getColor(R.color.green));
                                etTruckType.setText("Open - "+getString(R.string.truck_type_open_8));
                                dialog.dismiss();
                                dialog1.dismiss();
                                etMaterialType.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });

                llTruckTypeContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder1=new AlertDialog.Builder(getActivity());
                        View view2 = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_truck_type_container, null, false);
                        final TextView tvDialogTruckTypeContainerOne = view2.findViewById(R.id.tv_dialog_truck_type_container_one);
                        final TextView tvDialogTruckTypeContainerTwo = view2.findViewById(R.id.tv_dialog_truck_type_container_two);
                        final TextView tvDialogTruckTypeContainerThree = view2.findViewById(R.id.tv_dialog_truck_type_container_three);
                        final TextView tvDialogTruckTypeContainerFour = view2.findViewById(R.id.tv_dialog_truck_type_container_four);
                        final TextView tvDialogTruckTypeContainerFive = view2.findViewById(R.id.tv_dialog_truck_type_container_five);
                        final TextView tvDialogTruckTypeContainerSix = view2.findViewById(R.id.tv_dialog_truck_type_container_six);
                        final TextView tvDialogTruckTypeContainerSeven = view2.findViewById(R.id.tv_dialog_truck_type_container_seven);
                        final TextView tvDialogTruckTypeContainerEight = view2.findViewById(R.id.tv_dialog_truck_type_container_eight);
                        builder1.setView(view2);
                        final AlertDialog dialog1 = builder1.show();
                        tvDialogTruckTypeContainerOne.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tvDialogTruckTypeContainerOne.setBackgroundColor(getResources().getColor(R.color.green));
                                etTruckType.setText("Container - "+getString(R.string.truck_type_container_1));
                                dialog.dismiss();
                                dialog1.dismiss();
                                etMaterialType.setVisibility(View.VISIBLE);

                            }
                        });
                        tvDialogTruckTypeContainerTwo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tvDialogTruckTypeContainerTwo.setBackgroundColor(getResources().getColor(R.color.green));
                                etTruckType.setText("Container - "+getString(R.string.truck_type_container_2));
                                dialog.dismiss();
                                dialog1.dismiss();
                                etMaterialType.setVisibility(View.VISIBLE);

                            }
                        });
                        tvDialogTruckTypeContainerThree.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tvDialogTruckTypeContainerThree.setBackgroundColor(getResources().getColor(R.color.green));
                                etTruckType.setText("Container - "+getString(R.string.truck_type_container_3));
                                dialog.dismiss();
                                dialog1.dismiss();
                                etMaterialType.setVisibility(View.VISIBLE);

                            }
                        });
                        tvDialogTruckTypeContainerFour.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tvDialogTruckTypeContainerFour.setBackgroundColor(getResources().getColor(R.color.green));
                                etTruckType.setText("Container - "+getString(R.string.truck_type_container_4));
                                dialog.dismiss();
                                dialog1.dismiss();
                                etMaterialType.setVisibility(View.VISIBLE);

                            }
                        });
                        tvDialogTruckTypeContainerFive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tvDialogTruckTypeContainerFive.setBackgroundColor(getResources().getColor(R.color.green));
                                etTruckType.setText("Container - "+getString(R.string.truck_type_container_5));
                                dialog.dismiss();
                                dialog1.dismiss();
                                etMaterialType.setVisibility(View.VISIBLE);

                            }
                        });
                        tvDialogTruckTypeContainerSix.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tvDialogTruckTypeContainerSix.setBackgroundColor(getResources().getColor(R.color.green));
                                etTruckType.setText("Container - "+getString(R.string.truck_type_container_6));
                                dialog.dismiss();
                                dialog1.dismiss();
                                etMaterialType.setVisibility(View.VISIBLE);

                            }
                        });
                        tvDialogTruckTypeContainerSeven.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tvDialogTruckTypeContainerSeven.setBackgroundColor(getResources().getColor(R.color.green));
                                etTruckType.setText("Container - "+getString(R.string.truck_type_container_7));
                                dialog.dismiss();
                                dialog1.dismiss();
                                etMaterialType.setVisibility(View.VISIBLE);

                            }
                        });
                        tvDialogTruckTypeContainerEight.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tvDialogTruckTypeContainerEight.setBackgroundColor(getResources().getColor(R.color.green));
                                etTruckType.setText("Container - "+getString(R.string.truck_type_container_8));
                                dialog.dismiss();
                                dialog1.dismiss();
                                etMaterialType.setVisibility(View.VISIBLE);

                            }
                        });
                    }
                });

                llTruckTypeTrailer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder1=new AlertDialog.Builder(getActivity());
                        View view2 = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_truck_type_trailer, null, false);
                        final TextView tvDialogTruckTypeTrailerOne = view2.findViewById(R.id.tv_dialog_truck_type_trailer_one);
                        final TextView tvDialogTruckTypeTrailerTwo = view2.findViewById(R.id.tv_dialog_truck_type_trailer_two);
                        final TextView tvDialogTruckTypeTrailerThree = view2.findViewById(R.id.tv_dialog_truck_type_trailer_three);
                        builder1.setView(view2);
                        final AlertDialog dialog1 = builder1.show();
                        tvDialogTruckTypeTrailerOne.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tvDialogTruckTypeTrailerOne.setBackgroundColor(getResources().getColor(R.color.green));
                                etTruckType.setText("Trailer - "+getString(R.string.truck_type_trailer_1));
                                dialog.dismiss();
                                dialog1.dismiss();
                                etMaterialType.setVisibility(View.VISIBLE);

                            }
                        });
                        tvDialogTruckTypeTrailerTwo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tvDialogTruckTypeTrailerTwo.setBackgroundColor(getResources().getColor(R.color.green));
                                etTruckType.setText("Trailer - "+getString(R.string.truck_type_trailer_2));
                                dialog.dismiss();
                                dialog1.dismiss();
                                etMaterialType.setVisibility(View.VISIBLE);

                            }
                        });
                        tvDialogTruckTypeTrailerThree.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tvDialogTruckTypeTrailerThree.setBackgroundColor(getResources().getColor(R.color.green));
                                etTruckType.setText("Trailer - "+getString(R.string.truck_type_trailer_3));
                                dialog.dismiss();
                                dialog1.dismiss();
                                etMaterialType.setVisibility(View.VISIBLE);

                            }
                        });
                    }
                });

            }
        });

        etMaterialType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.dialog_material_type,null,false);
                final LinearLayout llMaterialTypeOne = view1.findViewById(R.id.ll_material_type_1);
                final LinearLayout llMaterialTypeTwo = view1.findViewById(R.id.ll_material_type_2);
                final LinearLayout llMaterialTypeThree = view1.findViewById(R.id.ll_material_type_3);
                final LinearLayout llMaterialTypeFour = view1.findViewById(R.id.ll_material_type_4);
                final LinearLayout llMaterialTypeFive = view1.findViewById(R.id.ll_material_type_5);
                final LinearLayout llMaterialTypeSix = view1.findViewById(R.id.ll_material_type_6);
                final LinearLayout llMaterialTypeSeven = view1.findViewById(R.id.ll_material_type_7);
                final LinearLayout llMaterialTypeEight = view1.findViewById(R.id.ll_material_type_8);
                final LinearLayout llMaterialTypeNine = view1.findViewById(R.id.ll_material_type_9);
                final LinearLayout llMaterialTypeTen = view1.findViewById(R.id.ll_material_type_10);
                final LinearLayout llMaterialTypeEleven = view1.findViewById(R.id.ll_material_type_11);
                final LinearLayout llMaterialTypeTwelve = view1.findViewById(R.id.ll_material_type_12);
                builder.setView(view1);
                final AlertDialog dialog=builder.show();
                llMaterialTypeOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llMaterialTypeOne.setBackgroundColor(getResources().getColor(R.color.green));
                        etMaterialType.setText(getString(R.string.material_type_1));
                        dialog.dismiss();
                        etLoadingDate.setVisibility(View.VISIBLE);
                    }
                });
                llMaterialTypeTwo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llMaterialTypeTwo.setBackgroundColor(getResources().getColor(R.color.green));
                        etMaterialType.setText(getString(R.string.material_type_2));
                        dialog.dismiss();
                        etLoadingDate.setVisibility(View.VISIBLE);
                    }
                });
                llMaterialTypeThree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llMaterialTypeThree.setBackgroundColor(getResources().getColor(R.color.green));
                        etMaterialType.setText(getString(R.string.material_type_3));
                        dialog.dismiss();
                        etLoadingDate.setVisibility(View.VISIBLE);
                    }
                });
                llMaterialTypeFour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llMaterialTypeFour.setBackgroundColor(getResources().getColor(R.color.green));
                        etMaterialType.setText(getString(R.string.material_type_4));
                        dialog.dismiss();
                        etLoadingDate.setVisibility(View.VISIBLE);
                    }
                });
                llMaterialTypeFive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llMaterialTypeFive.setBackgroundColor(getResources().getColor(R.color.green));
                        etMaterialType.setText(getString(R.string.material_type_5));
                        dialog.dismiss();
                        etLoadingDate.setVisibility(View.VISIBLE);
                    }
                });
                llMaterialTypeSix.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llMaterialTypeSix.setBackgroundColor(getResources().getColor(R.color.green));
                        etMaterialType.setText(getString(R.string.material_type_6));
                        dialog.dismiss();
                        etLoadingDate.setVisibility(View.VISIBLE);
                    }
                });
                llMaterialTypeSeven.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llMaterialTypeSeven.setBackgroundColor(getResources().getColor(R.color.green));
                        etMaterialType.setText(getString(R.string.material_type_7));
                        dialog.dismiss();
                        etLoadingDate.setVisibility(View.VISIBLE);
                    }
                });
                llMaterialTypeEight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llMaterialTypeEight.setBackgroundColor(getResources().getColor(R.color.green));
                        etMaterialType.setText(getString(R.string.material_type_8));
                        dialog.dismiss();
                        etLoadingDate.setVisibility(View.VISIBLE);
                    }
                });
                llMaterialTypeNine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llMaterialTypeNine.setBackgroundColor(getResources().getColor(R.color.green));
                        etMaterialType.setText(getString(R.string.material_type_9));
                        dialog.dismiss();
                        etLoadingDate.setVisibility(View.VISIBLE);
                    }
                });
                llMaterialTypeTen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llMaterialTypeTen.setBackgroundColor(getResources().getColor(R.color.green));
                        etMaterialType.setText(getString(R.string.material_type_10));
                        dialog.dismiss();
                        etLoadingDate.setVisibility(View.VISIBLE);
                    }
                });
                llMaterialTypeEleven.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llMaterialTypeEleven.setBackgroundColor(getResources().getColor(R.color.green));
                        etMaterialType.setText(getString(R.string.material_type_11));
                        dialog.dismiss();
                        etLoadingDate.setVisibility(View.VISIBLE);
                    }
                });
                llMaterialTypeTwelve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llMaterialTypeTwelve.setBackgroundColor(getResources().getColor(R.color.green));
                        etMaterialType.setText(getString(R.string.material_type_12));
                        dialog.dismiss();
                        etLoadingDate.setVisibility(View.VISIBLE);

                    }
                });
            }
        });


        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel(calendar);

            }
        };

        final Calendar calendar1 = Calendar.getInstance();

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar1.set(Calendar.HOUR_OF_DAY,i);
                calendar1.set(Calendar.MINUTE,i1);
                updateTimeLabel(calendar1);
            }


        };

        etLoadingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etLoadingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(),time,calendar1
                .get(Calendar.HOUR_OF_DAY),calendar1.get(Calendar.MINUTE),false).show();
            }
        });


        ArrayAdapter<String> spPaymentTypeAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, spPaymentTypeList);
        spPaymentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPaymentType.setAdapter(spPaymentTypeAdapter);
        spPaymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){

                    case 1:
                        spNoOfTrucks.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        spNoOfTrucks.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        spNoOfTrucks.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        spNoOfTrucks.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        spNoOfTrucks.setVisibility(View.VISIBLE);
                        break;
                    case 6:
                        spNoOfTrucks.setVisibility(View.VISIBLE);
                        break;
                    case 7:
                        spNoOfTrucks.setVisibility(View.VISIBLE);
                        break;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<String> spNoOfTrucksAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, spNoOfTrucksList);
        spNoOfTrucksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNoOfTrucks.setAdapter(spNoOfTrucksAdapter);
        spNoOfTrucks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        break;
                    case 1:
                        etRemarks.setVisibility(View.VISIBLE);
                        btPlaceOrder.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        etRemarks.setVisibility(View.VISIBLE);
                        btPlaceOrder.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        etRemarks.setVisibility(View.VISIBLE);
                        btPlaceOrder.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        etRemarks.setVisibility(View.VISIBLE);
                        btPlaceOrder.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        etRemarks.setVisibility(View.VISIBLE);
                        btPlaceOrder.setVisibility(View.VISIBLE);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        etRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                View view1= LayoutInflater.from(getContext()).inflate(R.layout.dialog_remarks,null,false);
                final EditText etRemarks1 = view1.findViewById(R.id.et_place_order_remarks);
                Button btRemarksSubmit = view1.findViewById(R.id.bt_remarks_submit);

                builder.setView(view1);
                final AlertDialog dialog=builder.show();

                btRemarksSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(etRemarks1.getText().toString().isEmpty()){
                            dialog.dismiss();
                        } else {
                            etRemarks.setText(etRemarks1.getText().toString());
                            dialog.dismiss();
                        }
                    }
                });

            }
        });

        btPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()){
                    ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Placing order...");
                    progressDialog.setCancelable(true);
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.show();
                    DatabaseReference reference = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(SharedPrefManager.getInstance(getContext()).getNumber())
                            .child("orders")
                            .child(generateHash(
                               etLoadingPoint.getText().toString(),
                               etTripDestination.getText().toString(),
                               etTruckType.getText().toString(),
                               etMaterialType.getText().toString(),
                               etLoadingDate.getText().toString(),
                               etLoadingTime.getText().toString()
                            ));
                    String remarks = "";
                    if (etRemarks.getText().toString().equals("")){
                        remarks = "None";
                    } else {
                        remarks = etRemarks.getText().toString();
                    }

                    String[] noOfTrucks = spNoOfTrucks.getSelectedItem().toString().split(": ");

                    Order order = new Order(
                            etLoadingPoint.getText().toString(),
                            etTripDestination.getText().toString(),
                            etTruckType.getText().toString(),
                            etMaterialType.getText().toString(),
                            etLoadingDate.getText().toString(),
                            etLoadingTime.getText().toString(),
                            spPaymentType.getSelectedItem().toString(),
                            noOfTrucks[1],
                            remarks,
                            "No",
                            SharedPrefManager.getInstance(getContext()).getNumber()
                    );

                   // Toast.makeText(getContext(), "order: "+ order, Toast.LENGTH_SHORT).show();
                    reference.setValue(order);
                    progressDialog.dismiss();
                    etTripDestination.setVisibility(View.GONE);
                    etTruckType.setVisibility(View.GONE);
                    etMaterialType.setVisibility(View.GONE);
                    etLoadingDate.setVisibility(View.GONE);
                    etLoadingTime.setVisibility(View.GONE);
                    spNoOfTrucks.setVisibility(View.GONE);
                    spPaymentType.setVisibility(View.GONE);
                    etRemarks.setVisibility(View.GONE);
                    btPlaceOrder.setVisibility(View.GONE);
                    etLoadingPoint.setText("");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Order placed succesfully");
                    builder.setMessage("To view your order status go to 'My Orders' section.");
                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //do nothing
                        }
                    });
                    builder.show();
                }
            }
        });

    }

    private String generateHash(String s, String s1, String s2, String s3, String s4, String s5) {
        int hash = 21;
        String main = s + s1 + s2 + s3 + s4 + s5;
        for (int i = 0; i < main.length(); i++) {
            hash = hash*31 + main.charAt(i);
        }
        if (hash < 0){
            hash = hash * -1;
        }
        return hash + "";
    }

    private String generateHash(String s) {
        int hash = 21;
        for (int i = 0; i < s.length(); i++) {
            hash = hash*31 + s.charAt(i);
        }
        if (hash < 0){
            hash = hash * -1;
        }
        return hash + "";
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    void updateTimeLabel(Calendar calendar1) {

        String myFormat = "hh:mm aa";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etLoadingTime.setText(sdf.format(calendar1.getTime()));
        spPaymentType.setVisibility(View.VISIBLE);

    }


    private void updateDateLabel(Calendar calendar) {

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etLoadingDate.setText(sdf.format(calendar.getTime()));
        etLoadingTime.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==LOADING_POINT_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                Place place = PlaceAutocomplete.getPlace(getActivity(),data);
                etLoadingPoint.setText(place.getAddress());
                etTripDestination.setVisibility(View.VISIBLE);
            } else if (resultCode==PlaceAutocomplete.RESULT_ERROR){
                Status status= PlaceAutocomplete.getStatus(getContext(),data);
                Toast.makeText(getContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode==TRIP_DESTINATION_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                Place place = PlaceAutocomplete.getPlace(getActivity(),data);
                etTripDestination.setText(place.getAddress());
                etTruckType.setVisibility(View.VISIBLE);
            } else if (resultCode==PlaceAutocomplete.RESULT_ERROR){
                Status status= PlaceAutocomplete.getStatus(getContext(),data);
                Toast.makeText(getContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
