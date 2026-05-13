package com.hazyala.pickday.kopo.ac.kr;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InviteMembersActivity extends AppCompatActivity {

    private TextView btnBack;
    private TextView btnRoomInfo;
    private TextView btnCopy;
    private TextView btnInviteDone;
    private TextView tvInviteLink;

    private LinearLayout btnKakaoShare;

    private Switch switchNotify;

    private final String inviteLink =
            "https://pickday.app/room/Abc123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_members);

        initView();
        setDummyData();
        setListener();
    }

    private void initView() {

        btnBack = findViewById(R.id.btnBack);
        btnRoomInfo = findViewById(R.id.btnRoomInfo);
        btnCopy = findViewById(R.id.btnCopy);
        btnInviteDone = findViewById(R.id.btnInviteDone);

        tvInviteLink = findViewById(R.id.tvInviteLink);

        btnKakaoShare = findViewById(R.id.btnKakaoShare);

        switchNotify = findViewById(R.id.switchNotify);
    }

    private void setDummyData() {

        tvInviteLink.setText(inviteLink);

        switchNotify.setChecked(true);
    }

    private void setListener() {

        btnBack.setOnClickListener(v -> {
            finish();
        });

        btnRoomInfo.setOnClickListener(v -> {

            Intent intent =
                    new Intent(InviteMembersActivity.this,
                            RoomDetailActivity.class);

            startActivity(intent);
        });

        btnCopy.setOnClickListener(v -> {

            ClipboardManager clipboardManager =
                    (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

            ClipData clipData =
                    ClipData.newPlainText("invite_link", inviteLink);

            if (clipboardManager != null) {

                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(
                        InviteMembersActivity.this,
                        "초대 링크가 복사되었어요!",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        btnKakaoShare.setOnClickListener(v -> {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            shareIntent.setType("text/plain");

            shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "PickDay 초대 링크\n" + inviteLink
            );

            startActivity(Intent.createChooser(
                    shareIntent,
                    "공유하기"
            ));
        });

        switchNotify.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (isChecked) {

                        Toast.makeText(
                                this,
                                "참여자 알림이 켜졌어요",
                                Toast.LENGTH_SHORT
                        ).show();

                    } else {

                        Toast.makeText(
                                this,
                                "참여자 알림이 꺼졌어요",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );

        btnInviteDone.setOnClickListener(v -> {

            Intent intent =
                    new Intent(InviteMembersActivity.this,
                            HomeActivity.class);

            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
            );

            startActivity(intent);

            finish();
        });
    }
}