package uk.co.appoly.sceneform.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LineViewActivity extends AppCompatActivity{
    private static final int MAX_ANCHORS = 2;
    private static final double MIN_OPENGL_VERSION = 3.0d;
    private static final String TAG = "LineViewActivity";
    private AnchorNode anchorNode;
    private List<AnchorNode> anchorNodeList = new ArrayList();
    private ModelRenderable andyRenderable;
    private ArFragment arFragment;
    private AnchorNode currentSelectedAnchorNode = null;
    private ModelRenderable foxRenderable;
    private Node nodeForLine;
    private Integer numberOfAnchors = Integer.valueOf(0);

   /* @Override
    public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
        lambdaonCreate4(hitResult, plane, motionEvent);
    }*/

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
       // if (checkIsSupportedDeviceOrFinish(this)) {
            setContentView(R.layout.activity_ux);
            this.arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(
                    R.id.ux_fragment);

            ModelRenderable.builder()
                    .setSource(this, R.raw.andy)
                    .build()
                    .thenAccept(renderable -> andyRenderable = renderable)
                    .exceptionally(throwable -> {
                        Toast toast =
                                Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return null;
                    });

            /*ModelRenderable.builder()
                    .setSource(this, R.raw.arcticfox_posed)
                    .build()
                    .thenAccept(renderable -> foxRenderable = renderable)
                    .exceptionally(throwable -> {
                        Toast toast =
                                Toast.makeText(this, "Unable to load arcticfox_posed" +
                                        " renderable", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return null;
                    });*/

            arFragment.getArSceneView().getScene()
                    .setOnPeekTouchListener(new Scene.OnPeekTouchListener() {
                        @Override
                        public void onPeekTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
                            Log.d(TAG, "handleOnTouch");
                            handleOnTouch(hitTestResult, motionEvent);
                        }
                    });


            arFragment.setOnTapArPlaneListener(
                    (HitResult hitresult, Plane plane, MotionEvent motionevent) -> {
                        if (andyRenderable == null) {
                            return;
                        }
                        Anchor anchor = hitresult.createAnchor();
                        AnchorNode anchorNode = new AnchorNode(anchor);
                        anchorNode.setParent(arFragment.getArSceneView().getScene());

                        TransformableNode lamp = new TransformableNode(arFragment.getTransformationSystem());
                        lamp.setParent(anchorNode);
                        lamp.setRenderable(andyRenderable);
                        lamp.select();
                    }
            );

            (findViewById(R.id.back_buttom)).setOnClickListener(new C03181());
            (findViewById(R.id.forward_buttom)).setOnClickListener(new C03192());
            (findViewById(R.id.left_button)).setOnClickListener(new C03203());
            (findViewById(R.id.right_button)).setOnClickListener(new C03214());
            (findViewById(R.id.up_button)).setOnClickListener(new C03225());
            (findViewById(R.id.down_button)).setOnClickListener(new C03236());
            (findViewById(R.id.draw_buttom)).setOnClickListener(new C03247());
            (findViewById(R.id.delete_buttom)).setOnClickListener(new C03258());
       // }
    }


    private void handleOnTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
        Log.d(TAG, "handleOnTouch");
        if (motionEvent.getAction() == 1) {
            if (hitTestResult.getNode() != null) {
                Log.d(TAG, "handleOnTouch hitTestResult.getNode() != null");
                Renderable renderable = hitTestResult.getNode().getRenderable();
                if (renderable == andyRenderable) {
                    if (currentSelectedAnchorNode != null) {
                        currentSelectedAnchorNode.setRenderable(andyRenderable);
                    }
                    Renderable renderable1 = this.andyRenderable.makeCopy();
                    renderable1.getMaterial().setFloat3("baseColorTint", new Color(android.graphics.Color.rgb(255, 0, 0)));
                    hitTestResult.getNode().setRenderable(renderable1);
                    currentSelectedAnchorNode = (AnchorNode) hitTestResult.getNode();
                }
                return;
            }
            Log.d(TAG, "adding Andy in fornt of camera");
            if (this.numberOfAnchors.intValue() < 2) {
                Frame arFrame = arFragment.getArSceneView().getArFrame();
                this.numberOfAnchors.intValue();
                AnchorNode node = new AnchorNode(this.arFragment.getArSceneView().getSession()
                        .createAnchor(arFrame.getCamera().getPose()
                                .compose(Pose.makeTranslation(0.0f, 0.0f, -1.0f))
                                .extractTranslation()));

                node.setRenderable(this.andyRenderable);
                addAnchorNode(node);
                currentSelectedAnchorNode = node;
            } else {
                Log.d(TAG, "MAX_ANCHORS exceeded");
            }
        }
    }


    class C03181 implements OnClickListener {
        C03181() {
        }

        public void onClick(View view) {
            Log.d(LineViewActivity.TAG, "Moving anchor back");
            if (LineViewActivity.this.currentSelectedAnchorNode != null) {
                Session session = arFragment.getArSceneView().getSession();
                Anchor currentAnchor = currentSelectedAnchorNode.getAnchor();
                Pose oldPose = currentAnchor.getPose();
                Pose newPose = oldPose.compose(Pose.makeTranslation(0.0f, 0.0f, -0.05f));
                currentSelectedAnchorNode = moveRenderable(currentSelectedAnchorNode, newPose);
            }
        }
    }

    class C03192 implements OnClickListener {
        C03192() {
        }

        public void onClick(View view) {
            Log.d(LineViewActivity.TAG, "Moving anchor forward");
            if (LineViewActivity.this.currentSelectedAnchorNode != null) {
                Session session = arFragment.getArSceneView().getSession();
                Anchor currentAnchor = currentSelectedAnchorNode.getAnchor();
                Pose oldPose = currentAnchor.getPose();
                Pose newPose = oldPose.compose(Pose.makeTranslation(0.0f, 0.0f, 0.05f));
                currentSelectedAnchorNode = moveRenderable(currentSelectedAnchorNode, newPose);
            }
        }
    }

    class C03203 implements OnClickListener {
        C03203() {
        }

        public void onClick(View view) {
            Log.d(LineViewActivity.TAG, "Moving anchor left");
            if (LineViewActivity.this.currentSelectedAnchorNode != null) {
                Session session = arFragment.getArSceneView().getSession();
                Anchor currentAnchor = currentSelectedAnchorNode.getAnchor();
                Pose oldPose = currentAnchor.getPose();
                Pose newPose = oldPose.compose(Pose.makeTranslation(-0.05f, 0.0f, 0.0f));
                currentSelectedAnchorNode = moveRenderable(currentSelectedAnchorNode, newPose);
            }
        }
    }

    class C03214 implements OnClickListener {
        C03214() {
        }

        public void onClick(View view) {
            Log.d(LineViewActivity.TAG, "Moving anchor Right");
            if (LineViewActivity.this.currentSelectedAnchorNode != null) {
                Session session = arFragment.getArSceneView().getSession();
                Anchor currentAnchor = currentSelectedAnchorNode.getAnchor();
                Pose oldPose = currentAnchor.getPose();
                Pose newPose = oldPose.compose(Pose.makeTranslation(0.05f, 0.0f, 0.0f));
                currentSelectedAnchorNode = moveRenderable(currentSelectedAnchorNode, newPose);
            }
        }
    }

    class C03225 implements OnClickListener {
        C03225() {
        }

        public void onClick(View view) {
            Log.d(LineViewActivity.TAG, "Moving anchor Up");
            if (LineViewActivity.this.currentSelectedAnchorNode != null) {
                Session session = arFragment.getArSceneView().getSession();
                Anchor currentAnchor = currentSelectedAnchorNode.getAnchor();
                Pose oldPose = currentAnchor.getPose();
                Pose newPose = oldPose.compose(Pose.makeTranslation(0.0f, 0.05f, 0.0f));
                currentSelectedAnchorNode = moveRenderable(currentSelectedAnchorNode, newPose);
            }
        }
    }

    class C03236 implements OnClickListener {
        C03236() {
        }

        public void onClick(View view) {
            Log.d(LineViewActivity.TAG, "Moving anchor Down");
            if (LineViewActivity.this.currentSelectedAnchorNode != null) {
                Session session = arFragment.getArSceneView().getSession();
                Anchor currentAnchor = currentSelectedAnchorNode.getAnchor();
                Pose oldPose = currentAnchor.getPose();
                Pose newPose = oldPose.compose(Pose.makeTranslation(0.0f, -0.05f, 0.0f));
                currentSelectedAnchorNode = moveRenderable(currentSelectedAnchorNode, newPose);
            }
        }
    }

    class C03247 implements OnClickListener {
        C03247() {
        }

        public void onClick(View view) {
            Log.d(LineViewActivity.TAG, "drawing line");
            if (LineViewActivity.this.numberOfAnchors.intValue() == 2) {
                drawLine(anchorNodeList.get(0), anchorNodeList.get(1));
            }
        }
    }



    class C03258 implements OnClickListener {
        C03258() {
        }

        public void onClick(View view) {
            Log.d(LineViewActivity.TAG, "Deleteing anchor");
            if (LineViewActivity.this.numberOfAnchors.intValue() < 1) {
                Toast.makeText(LineViewActivity.this, "All nodes deleted",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            removeAnchorNode(currentSelectedAnchorNode);
            LineViewActivity.this.currentSelectedAnchorNode = null;
            removeLine(nodeForLine);
        }
    }

    static void lambdaonCreate4(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
    }

    private void removeAnchorNode(AnchorNode anchorNode) {
        if (anchorNode != null) {
            arFragment.getArSceneView().getScene().removeChild(anchorNode);
            anchorNodeList.remove(anchorNode);
            anchorNode.getAnchor().detach();
            anchorNode.setParent(null);
            //numberOfAnchors = Integer.valueOf(this.numberOfAnchors.intValue() - 1);
            numberOfAnchors=numberOfAnchors-1;
            return;
        }
        Toast.makeText(this, "Delete - no node selected! Touch a node to select it.",
                Toast.LENGTH_SHORT).show();
    }

    private void removeLine(Node node) {
        Log.e(TAG, "removeLine");
        if (node != null) {
            Log.e(TAG, "removeLine lineToRemove is not mull");
            arFragment.getArSceneView().getScene().removeChild(node);
            node.setParent(null);
        }
    }

    private void addAnchorNode(AnchorNode anchorNode) {
        anchorNode.setParent(this.arFragment.getArSceneView().getScene());
        anchorNodeList.add(anchorNode);
        numberOfAnchors = Integer.valueOf(this.numberOfAnchors.intValue() + 1);
    }

    private AnchorNode moveRenderable(AnchorNode anchorNode, Pose pose) {
        if (anchorNode != null) {
            arFragment.getArSceneView().getScene().removeChild(anchorNode);
            anchorNodeList.remove(anchorNode);
            arFragment.getArSceneView().getArFrame();
            anchorNode = new AnchorNode(this.arFragment.getArSceneView().getSession().createAnchor
                    (pose.extractTranslation()));
            anchorNode.setRenderable(this.andyRenderable);
            anchorNode.setParent(this.arFragment.getArSceneView().getScene());
            anchorNodeList.add(anchorNode);
            removeLine(this.nodeForLine);
            return anchorNode;
        }
        Log.d(TAG, "moveRenderable - markAnchorNode was null, the little £$%^...");
        return null;
    }

    private void drawLine(AnchorNode anchorNode, AnchorNode anchorNode2) {
        Log.d(TAG, "drawLine");
        Vector3 worldPosition = anchorNode.getWorldPosition();
        Vector3 worldPosition2 = anchorNode2.getWorldPosition();
        Vector3 subtract = Vector3.subtract(worldPosition, worldPosition2);

        MaterialFactory.makeOpaqueWithColor(getApplicationContext(),
                new Color(0.0f, 255.0f, 244.0f)).
                thenAccept(new SetAnchors(
                        subtract, anchorNode2, anchorNode,
                        worldPosition, worldPosition2,
                        Quaternion.lookRotation(subtract.normalized(), Vector3.up())));
    }


    public final class SetAnchors implements Consumer {
        private final Vector3 f$1;
        private final AnchorNode f$2;
        private final AnchorNode f$3;
        private final Vector3 f$4;
        private final Vector3 f$5;
        private final Quaternion f$6;

        public SetAnchors(Vector3 vector3,
                          AnchorNode anchorNode,
                          AnchorNode anchorNode2,
                          Vector3 vector32,
                          Vector3 vector33,
                          Quaternion quaternion) {
            this.f$1 = vector3;
            this.f$2 = anchorNode;
            this.f$3 = anchorNode2;
            this.f$4 = vector32;
            this.f$5 = vector33;
            this.f$6 = quaternion;
        }

        public final void accept(Object obj) {
            getNodeAndAnchors(this.f$1, this.f$2, this.f$3, this.f$4,
                    this.f$5, this.f$6, (Material) obj);
        }
    }

    public void getNodeAndAnchors(Vector3 vector3,
                                  AnchorNode anchorNode,
                                  AnchorNode anchorNode2,
                                  Vector3 vector32,
                                  Vector3 vector33,
                                  Quaternion quaternion,
                                  Material material) {
        Log.d(TAG, "drawLine insie .thenAccept");
        Renderable vcr = ShapeFactory.makeCube(new Vector3(0.01f, 0.01f,
                vector3.length()), Vector3.zero(), material);
        anchorNode.getAnchor();
        this.nodeForLine = new Node();
        this.nodeForLine.setParent(anchorNode2);
        this.nodeForLine.setRenderable(vcr);
        this.nodeForLine.setWorldPosition(Vector3.add(vector32, vector33).scaled(0.5f));
        this.nodeForLine.setWorldRotation(quaternion);
    }
}



