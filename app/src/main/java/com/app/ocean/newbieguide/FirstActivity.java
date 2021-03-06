package com.app.ocean.newbieguide;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ocean.guide.NewbieGuide;
import com.app.ocean.guide.core.Controller;
import com.app.ocean.guide.listener.OnGuideChangedListener;
import com.app.ocean.guide.listener.OnHighlightDrewListener;
import com.app.ocean.guide.listener.OnLayoutInflatedListener;
import com.app.ocean.guide.listener.OnPageChangedListener;
import com.app.ocean.guide.model.GuidePage;
import com.app.ocean.guide.model.HighLight;
import com.app.ocean.guide.model.HighlightOptions;
import com.app.ocean.guide.model.RelativeGuide;

public class FirstActivity extends AppCompatActivity {
    private Button btn_skip2;
    private Button btn_skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        btn_skip2 = findViewById(R.id.btn_skip2);
        btn_skip = findViewById(R.id.btn_skip);
        btn_skip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newbieGuide(btn_skip2);
            }
        });
        //简单使用
        final Button btnSimple = findViewById(R.id.btn_simple);
        btnSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewbieGuide.with(FirstActivity.this)
                        .setLabel("guide1")
//                        .setShowCounts(3)//控制次数
                        .alwaysShow(true)//总是显示，调试时可以打开
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(btnSimple)
                                .addHighLight(new RectF(0, 800, 200, 1200))
                                .setLayoutRes(R.layout.view_guide_simple))
                        .show();
            }
        });
        //对话框形式
        final Button btnDialog = (Button) findViewById(R.id.btn_dialog);
        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewbieGuide.with(FirstActivity.this)
                        .setLabel("guide2")
                        .alwaysShow(true)//总是显示，调试时可以打开
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(btnDialog)
                                .setEverywhereCancelable(false)//是否点击任意位置消失引导页
                                .setLayoutRes(R.layout.view_guide_dialog, R.id.btn_ok)
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {

                                    @Override
                                    public void onLayoutInflated(View view, Controller controller) {
                                        TextView tv = view.findViewById(R.id.tv_text);
                                        tv.setText("this like dialog");
                                    }
                                }))
                        .show();
            }
        });
        //设置anchor 及 自定义绘制图形
        final View anchorView = findViewById(R.id.ll_anchor);
        final Button btnAnchor = (Button) findViewById(R.id.btn_anchor);
        btnAnchor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HighlightOptions options = new HighlightOptions.Builder()
                        .setOnHighlightDrewListener(new OnHighlightDrewListener() {
                            @Override
                            public void onHighlightDrew(Canvas canvas, RectF rectF) {
                                Paint paint = new Paint();
                                paint.setColor(Color.WHITE);
                                paint.setStyle(Paint.Style.STROKE);
                                paint.setStrokeWidth(10);
                                paint.setPathEffect(new DashPathEffect(new float[]{20, 20}, 0));
                                canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2 + 10, paint);
                            }
                        })
                        .build();
                NewbieGuide.with(FirstActivity.this)
                        .setLabel("anchor")
                        .anchor(anchorView)
                        .alwaysShow(true)//总是显示，调试时可以打开
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLightWithOptions(btnAnchor, HighLight.Shape.CIRCLE, options)
                                .setLayoutRes(R.layout.view_guide_anchor))
                        .show();
            }
        });
        //监听
        final Button btnListener = findViewById(R.id.btn_listener);
        btnListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewbieGuide.with(FirstActivity.this)
                        .setLabel("listener")
                        .alwaysShow(true)//总是显示，调试时可以打开
                        .setOnGuideChangedListener(new OnGuideChangedListener() {
                            @Override
                            public void onShowed(Controller controller) {
                                Toast.makeText(FirstActivity.this, "引导层显示", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onRemoved(Controller controller) {
                                Toast.makeText(FirstActivity.this, "引导层消失", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSkiped() {

                            }
                        })
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(btnListener))
                        .show();
            }
        });

        findViewById(R.id.btn_multi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.start(FirstActivity.this);
            }
        });

        findViewById(R.id.btn_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewActivity.start(FirstActivity.this);
            }
        });
        findViewById(R.id.btn_scroll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollViewActivity.start(FirstActivity.this);
            }
        });

        final View btnRelative = findViewById(R.id.btn_relative);
        btnRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HighlightOptions options = new HighlightOptions.Builder()
                        .setRelativeGuide(new RelativeGuide(R.layout.view_relative_guide, Gravity.LEFT, 100) {
                            @Override
                            protected void onLayoutInflated(View view, Controller controller) {
                                TextView textView = view.findViewById(R.id.tv);
                                textView.setText("inflated");
                            }
                        })
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(FirstActivity.this, "highlight click", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();
                GuidePage page = GuidePage.newInstance()
                        .addHighLightWithOptions(btnRelative, options);
                NewbieGuide.with(FirstActivity.this)
                        .setLabel("relative")
                        .alwaysShow(true)//总是显示，调试时可以打开
                        .addGuidePage(page)
                        .show();
            }
        });

        findViewById(R.id.btn_rect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewbieGuide.with(FirstActivity.this)
                        .setLabel("rect")
                        .alwaysShow(true)//总是显示，调试时可以打开
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(new RectF(0, 800, 500, 1000))
                        )
                        .show();
            }
        });
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HighlightOptions options2 = new HighlightOptions.Builder()
                        .setOnHighlightDrewListener(new OnHighlightDrewListener() {
                            @Override
                            public void onHighlightDrew(Canvas canvas, RectF rectF) {
                                Paint paint = new Paint();
                                paint.setColor(Color.TRANSPARENT);
//                        paint.setStyle(Paint.Style.STROKE);
//                        paint.setStrokeWidth(5);
                                paint.setPathEffect(new DashPathEffect(new float[]{20, 20}, 0));
                                canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2 + 10, paint);

                            }
                        })
                        .build();
                NewbieGuide.with(FirstActivity.this)
                        .setLabel("pageSkip")//设置引导层标示区分不同引导层，必传！否则报错
//                .anchor(anchor)
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLightWithOptions(btn_skip, HighLight.Shape.CIRCLE, options2)
                                .setLayoutResSkip(R.layout.view_guide_custom2, R.id.tvSkip))
                        .setOnGuideChangedListener(new OnGuideChangedListener() {
                            @Override
                            public void onShowed(Controller controller) {
//                        Toast.makeText(FirstActivity.this, "onShowed" , Toast.LENGTH_SHORT).show();
                                //引导层显示
                            }

                            @Override
                            public void onRemoved(Controller controller) {
//                        Toast.makeText(FirstActivity.this, "onRemoved" , Toast.LENGTH_SHORT).show();
                                //引导层消失（多页切换不会触发）
                            }

                            @Override
                            public void onSkiped() {
                                Toast.makeText(FirstActivity.this, "onSkiped", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .alwaysShow(true)//是否每次都显示引导层，默认false，只显示一次
                        .show();//显示引导层(至少需要一页引导页才能显示)
            }
        });

    }


    private void newbieGuide(View view) {
        HighlightOptions options3 = new HighlightOptions.Builder()
                .setOnHighlightDrewListener(new OnHighlightDrewListener() {
                    @Override
                    public void onHighlightDrew(Canvas canvas, RectF rectF) {
                        Paint paint = new Paint();
                        paint.setColor(Color.WHITE);
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setStrokeWidth(5);
                        paint.setPathEffect(new DashPathEffect(new float[]{20, 20}, 0));
                        float left = rectF.centerX() - rectF.width() / 2;
                        float top = rectF.centerY() - rectF.height() / 2;
                        float tmp = 20;
                        float mRadius = 20;
                        canvas.drawRoundRect(new RectF(left - tmp, top - tmp, rectF.width() + left + tmp, rectF.height() + top + tmp), mRadius, mRadius, paint);// + tmp
                    }
                })
                .build();
        NewbieGuide.with(FirstActivity.this)
                .setLabel("guide3")
//                        .setShowCounts(3)//控制次数
                .alwaysShow(true)//总是显示，调试时可以打开
                .addGuidePage(GuidePage.newInstance()
                        .addHighLightWithOptions(view, HighLight.Shape.ROUND_RECTANGLE, options3)
                        .setLayoutRes(R.layout.view_guide_custom3)
                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                            @Override
                            public void onLayoutInflated(View view, Controller controller) {
                                TextView tvSkip = view.findViewById(R.id.tvSkip);
                                tvSkip.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(FirstActivity.this, "onClick--guide4", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }))

                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {
//                        ToastUtils.makeToast("引导层显示");
                    }

                    @Override
                    public void onRemoved(Controller controller) {
//                        ToastUtils.makeToast("引导层消失");
                    }

                    @Override
                    public void onSkiped() {
                        NewbieGuide.setSkipLabel(FirstActivity.this, "guide4");
                        Toast.makeText(FirstActivity.this, "onSkiped--guide4", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

}
