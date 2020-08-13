package com.wood.calculator_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int[] numberRes = { R.id.B0,R.id.B1,R.id.B2,R.id.B3,R.id.B4,R.id.B5,R.id.B6,R.id.B7,R.id.B8,R.id.B9 };
    private int[] calculateRes = {R.id.B_plus,R.id.B_minus,R.id.B_multiply,R.id.B_except};
    private View[] btnNumber = new View[numberRes.length];
    private View[] btnCalculate = new View[calculateRes.length];
    private TextView showResult;
    private TextView showCalculate;
    private StringBuilder number_str;
    private String symbolCalculate;

    private ArrayList<String> data;
    private boolean isResult;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //關閉最上面的狀態bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //關閉APP標題bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        showResult = findViewById(R.id.show1);
        showCalculate = findViewById(R.id.show2);

        data = new ArrayList<>();
        number_str = new StringBuilder();
        isResult = false;

        initView();
    }
    private void initView()//初始化畫面
    {
        for (int i = 0; i < numberRes.length; i++)
        {
            btnNumber[i] = findViewById(numberRes[i]);//按鈕0~9
        }
        for (int j = 0; j < calculateRes.length; j++)
        {
            btnCalculate[j] = findViewById(calculateRes[j]);
        }
    }

    public void Input(View view)//設定輸入鍵
    {
        if(isResult)
        {
            number_str = new StringBuilder();
            isResult = false;
        }
        //比對輸入鍵
        for (int i = 0; i < btnNumber.length; i++)//(數字0~9)
        {
            if (view == btnNumber[i])//如果btnNumber等於按下的按紐(0~9其中之一)
            {
                number_str.append(i);
                break;
            }
        }

        String number = number_str.toString().replaceFirst("^0+(?!$)", "");
        number = (number.contains(".") && number.charAt(0) == '.')? ("0" + number) : (number);

        showResult.setText(number + " ");
        number_str = new StringBuilder();
        number_str.append(number);
    }

    public void Calculate(View view)
    {
        if(number_str.length() == 0)
        {
            number_str.append(0);
        }
        double value = Double.parseDouble(number_str.toString());
        number_str = new StringBuilder();
        data.add(value + "");

        for (int i = 0; i < btnCalculate.length; i++)
        {
            if (view == btnCalculate[i])
            {
                switch (i)
                {
                    case 0:
                        symbolCalculate = " + ";
                        break;
                    case 1:
                        symbolCalculate = " - ";
                        break;
                    case 2:
                        symbolCalculate = " * ";
                        break;
                    case 3:
                        symbolCalculate = " / ";
                        break;
                }
            }
        }

        if(data.get(data.size()-1)!=" + " && data.get(data.size()-1)!=" - " && data.get(data.size()-1)!=" * " && data.get(data.size()-1)!=" / ")
        {
            data.add(symbolCalculate);
        }
        else
        {
            data.remove(data.size()-1);
            data.add(symbolCalculate);
        }

        data = (data.size() >= 3)? getResult(data) : data;
        showResult.setText(getRealNumber(Double.parseDouble((data.get(0))))+" ");
        showCalculate.setText(getRealNumber(Double.parseDouble((data.get(0)))) + symbolCalculate);
    }

    public void Result(View view)
    {
        if(!(number_str.length() == 0 || data.size() == 0))
        {
            double value = Double.parseDouble(number_str.toString());
            data.add(value + "");

            if(data.size() >= 3)
            {
                showCalculate.setText(getRealNumber(Double.parseDouble((data.get(0)))) + (data.get(1)) + getRealNumber(Double.parseDouble((data.get(2))))  + " =  ");
                data = getResult(data);
            }
            String ans = data.get(0);

            showResult.setText(getRealNumber(Double.parseDouble((data.get(0))))+" ");

            data.removeAll(data);
            number_str = new StringBuilder();
            number_str.append(ans);
            isResult = true;
        }
    }

    public void Point(View view)
    {
        if(!number_str.toString().contains("."))
        {
            String number = "";
            if(number_str.length() == 0)
            {
                number_str.append("0.");
                number = number_str.toString();
            }
            else
            {
                if(Double.parseDouble(number_str.toString()) == 0)
                {
                    number_str = new StringBuilder();
                    number_str.append("0.");
                    number = number_str.toString();
                }
                else
                {
                    number = number_str.toString().replaceFirst("^0+(?!$)", "");
                    number = (Math.abs(Double.parseDouble(number)) < 1)?
                            ("0" + number) : (getRealNumber(Double.parseDouble(number))+".");
                }
            }
            number_str = new StringBuilder();
            number_str.append(number);
            showResult.setText(number+" ");
        }
    }

    public void Reverse(View view)
    {
        if(number_str.length() != 0)
        {
            double value = Double.parseDouble(number_str.toString());
            value = value * (-1);
            showResult.setText(getRealNumber(value) + " ");
            number_str = new StringBuilder();
            number_str.append(value);
        }
    }

    public void Square(View view)
    {
        if(number_str.length() != 0)
        {
            double value = Double.parseDouble(number_str.toString());
            value = value * value;
            showResult.setText(getRealNumber(value)+ " ");
            number_str = new StringBuilder();
            number_str.append(value);
        }
    }

    public void Back(View view)
    {
        Boolean condition = (number_str.length() <= 1 ||
                number_str.toString().equals("Infinity") ||
                number_str.toString().equals("-Infinity") ||
                number_str.toString().equals("NaN"));
        if(condition)
        {
            number_str = new StringBuilder();
            number_str.append(0);
        }
        else
        {
            number_str.deleteCharAt(number_str.length()-1);
        }
        showResult.setText(number_str.toString()+ " ");
    }

    public void Clear(View view)
    {
        showCalculate.setText("");
        showResult.setText("0 ");
        data.removeAll(data);
        number_str = new StringBuilder();
        isResult = false;
    }

    private static ArrayList<String> getResult(ArrayList<String> data)
    {
        double v = 0;
        double v0 = Double.parseDouble(data.get(0));
        double v2 = Double.parseDouble(data.get(2));
        String symbol = data.get(1);

        data.remove(0);
        data.remove(0);
        data.remove(0);

        switch (symbol)
        {
            case " + ":
                v = v0 + v2;
                break;
            case " - ":
                v = v0 - v2;
                break;
            case " * ":
                v = v0 * v2;
                break;
            case " / ":
                v = v0 / v2;
                break;
        }
        data.add(0, v + "");
        return data;
    }

    private static String getRealNumber(double n)
    {
        if(n == 0)
        {
            return 0 + "";
        }
        String ans = n + "";
        ans = ans.contains(".") ?
                ans.replaceAll("0+?$", "").replaceAll("\\.$","") : ans;

        return ans;
    }
}