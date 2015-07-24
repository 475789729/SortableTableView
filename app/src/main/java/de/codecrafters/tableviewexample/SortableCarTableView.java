package de.codecrafters.tableviewexample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.SortStateViewProviders;
import de.codecrafters.tableview.toolkit.TableDataRowColorisers;


/**
 * Created by Ingo on 18.07.2015.
 */
public class SortableCarTableView extends SortableTableView<Car> {

    private static List<Car> CAR_LIST = new ArrayList<>();

    static {
        CarProducer audi = new CarProducer(R.mipmap.audi, "Audi");
        Car audiA1 = new Car(audi, "A1", 150, 25000);
        Car audiA3 = new Car(audi, "A3", 120, 35000);
        Car audiA4 = new Car(audi, "A4", 210, 42000);
        Car audiA5 = new Car(audi, "S5", 333, 60000);
        Car audiA6 = new Car(audi, "A6", 250, 55000);
        Car audiA7 = new Car(audi, "A7", 420, 87000);
        Car audiA8 = new Car(audi, "A8", 320, 110000);

        CarProducer bmw = new CarProducer(R.mipmap.bmw, "BMW");
        Car bmw1 = new Car(bmw, "1er", 170, 25000);
        Car bmw3 = new Car(bmw, "3er", 230, 42000);
        Car bmwX3 = new Car(bmw, "X3", 230, 45000);
        Car bmw4 = new Car(bmw, "4er", 250, 39000);
        Car bmwM4 = new Car(bmw, "M4", 350, 60000);
        Car bmw5 = new Car(bmw, "5er", 230, 46000);

        CarProducer porsche = new CarProducer(R.mipmap.porsche, "Porsche");
        Car porsche911 = new Car(porsche, "911", 280, 45000);
        Car porscheCayman = new Car(porsche, "Cayman", 330, 52000);
        Car porscheCaymanGT4 = new Car(porsche, "Cayman GT4", 385, 86000);

        CAR_LIST.add(audiA3);
        CAR_LIST.add(audiA1);
        CAR_LIST.add(porscheCayman);
        CAR_LIST.add(audiA7);
        CAR_LIST.add(audiA8);
        CAR_LIST.add(audiA4);
        CAR_LIST.add(bmwX3);
        CAR_LIST.add(porsche911);
        CAR_LIST.add(bmw1);
        CAR_LIST.add(audiA6);
        CAR_LIST.add(audiA5);
        CAR_LIST.add(bmwM4);
        CAR_LIST.add(bmw5);
        CAR_LIST.add(porscheCaymanGT4);
        CAR_LIST.add(bmw3);
        CAR_LIST.add(bmw4);
    }


    public SortableCarTableView(Context context) {
        this(context, null);
    }

    public SortableCarTableView(Context context, AttributeSet attributes) {
        this(context, attributes, 0);
    }

    public SortableCarTableView(Context context, AttributeSet attributes, int styleAttributes) {
        super(context, attributes, styleAttributes);

        SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(context, "Hersteller", "Bezeichung", "Leistung", "Preis");
        simpleTableHeaderAdapter.setTextColor(0xCCFFFFFF);
        simpleTableHeaderAdapter.setPaddingTop(40);
        simpleTableHeaderAdapter.setPaddingBottom(40);
        setHeaderAdapter(simpleTableHeaderAdapter);

        setHeaderSortStateViewProvider(SortStateViewProviders.brightArrows());
        setDataRowColoriser(TableDataRowColorisers.alternatingRows(0x00C5CAE9, 0x33C5CAE9));

        setColumnWeight(0, 2);
        setColumnWeight(1, 4);
        setColumnWeight(2, 3);
        setColumnWeight(3, 2);

        setDataAdapter(new CarTableDataAdapter(getContext(), CAR_LIST));
        setColumnComparator(0, new CarProducerComparator());
        setColumnComparator(1, new CarNameComparator());
        setColumnComparator(2, new CarPsComparator());
        setColumnComparator(3, new CarPriceComparator());
        addDataClickListener(new CarClickListener());
    }

    private class CarClickListener implements TableDataClickListener<Car> {

        @Override
        public void onDataClicked(int rowIndex, Car clickedData) {
            String carString = clickedData.getProducer().getName() + " " + clickedData.getName();
            Toast.makeText(getContext(), carString, Toast.LENGTH_SHORT).show();
        }
    }


    private static class CarTableDataAdapter extends TableDataAdapter<Car> {

        private static final int TEXT_SIZE = 14;
        private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();

        public CarTableDataAdapter(Context context, List<Car> data) {
            super(context, data);
        }

        @Override
        public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
            Car data = getItem(rowIndex);
            View renderedView = null;

            switch (columnIndex) {
                case 0:
                    renderedView = renderProducerLogo(data);
                    break;
                case 1:
                    renderedView = renderCatName(data);
                    break;
                case 2:
                    renderedView = renderPower(data);
                    break;
                case 3:
                    renderedView = renderPrice(data);
                    break;
            }

            return renderedView;
        }

        private View renderPrice(Car car) {
            String priceString = PRICE_FORMATTER.format(car.getPrice()) + " €";

            TextView textView = new TextView(getContext());
            textView.setText(priceString);
            textView.setPadding(20, 10, 20, 10);
            textView.setTextSize(TEXT_SIZE);

            if (car.getPrice() < 50000) {
                textView.setTextColor(0xFF2E7D32);
            } else if (car.getPrice() > 100000) {
                textView.setTextColor(0xFFC62828);
            }

            return textView;
        }

        private View renderPower(Car car) {
            View view = getLayoutInflater().inflate(R.layout.table_cell_power, null, false);
            TextView kwView = (TextView) view.findViewById(R.id.kw_view);
            TextView psView = (TextView) view.findViewById(R.id.ps_view);

            kwView.setText(car.getKw() + " kW");
            psView.setText(car.getPs() + " PS");

            return view;
        }

        private View renderCatName(Car car) {
            return renderString(car.getName());
        }

        private View renderProducerLogo(Car car) {
            View view = getLayoutInflater().inflate(R.layout.table_cell_image, null, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            imageView.setImageResource(car.getProducer().getLogo());
            return view;
        }

        private View renderString(String value) {
            TextView textView = new TextView(getContext());
            textView.setText(value);
            textView.setPadding(20, 10, 20, 10);
            textView.setTextSize(TEXT_SIZE);
            return textView;
        }

    }


    private static class CarProducerComparator implements Comparator<Car> {

        @Override
        public int compare(Car car1, Car car2) {
            return car1.getProducer().getName().compareTo(car2.getProducer().getName());
        }
    }

    private static class CarPsComparator implements Comparator<Car> {

        @Override
        public int compare(Car car1, Car car2) {
            return car1.getPs() - car2.getPs();
        }
    }

    private static class CarNameComparator implements Comparator<Car> {

        @Override
        public int compare(Car car1, Car car2) {
            return car1.getName().compareTo(car2.getName());
        }
    }

    private static class CarPriceComparator implements Comparator<Car> {

        @Override
        public int compare(Car car1, Car car2) {
            if (car1.getPrice() < car2.getPrice()) return -1;
            if (car1.getPrice() > car2.getPrice()) return 1;
            return 0;
        }
    }

}
