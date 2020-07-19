using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Android;
using UnityEngine.UI;

public class SelectionManager : MonoBehaviour
{
    public GameObject ground;
    public GameObject[] buttons;
    public GameObject[] crosses;
    public GameObject DressPanel;
    public GameObject TexturePanel;
    public GameObject ShoePanel;
    public GameObject AccessoryPanel;
    public GameObject rotatePanel;
    public GameObject MainPanel;
    public GameObject dismissPanel;
    public bool back;
    public GameObject timer;

    private void Start()
    {
        if (!back) {
            buttons[buttons.Length - 1].transform.localScale = Vector3.zero;
            crosses[crosses.Length - 1].transform.localScale = Vector3.zero;
        }
        if (!Permission.HasUserAuthorizedPermission(Permission.ExternalStorageWrite))
        {
            Permission.RequestUserPermission(Permission.ExternalStorageWrite);
        }
        if (!Permission.HasUserAuthorizedPermission(Permission.ExternalStorageRead))
        {
            Permission.RequestUserPermission(Permission.ExternalStorageRead);
        }
        for (int i = 0; i < buttons.Length; i++) {
            buttons[i].SetActive(true);
        }
        for (int i = 0; i < crosses.Length; i++) {
            crosses[i].SetActive(false);
        }
        timer.SetActive(false);
        dismissPanel.SetActive(false);
        DressPanel.SetActive(false);
        TexturePanel.SetActive(false);
        ShoePanel.SetActive(false);
        AccessoryPanel.SetActive(false);
        rotatePanel.SetActive(false);
    }
    public void OnDressClick()
    {for (int i = 0; i < buttons.Length; i++) {
            buttons[i].SetActive(true);
        }
        for (int i = 0; i < crosses.Length; i++) {
            crosses[i].SetActive(false);
        }
        dismissPanel.SetActive(false);
        DressPanel.SetActive(false);
        TexturePanel.SetActive(false);
        ShoePanel.SetActive(false);
        AccessoryPanel.SetActive(false);
        rotatePanel.SetActive(false);
        dismissPanel.SetActive(true);
        DressPanel.SetActive(true);
        buttons[1].SetActive(false);
        crosses[0].SetActive(true);
    }
    private void Reset()
    {
        for (int i = 0; i < buttons.Length; i++)
        {
            buttons[i].SetActive(true);
        }
        for (int i = 0; i < crosses.Length; i++)
        {
            crosses[i].SetActive(false);
        }
        dismissPanel.SetActive(false);
        DressPanel.SetActive(false);
        TexturePanel.SetActive(false);
        ShoePanel.SetActive(false);
        AccessoryPanel.SetActive(false);
        rotatePanel.SetActive(false);
    }
    public void OnShoesClick()
    {
        Reset();
        dismissPanel.SetActive(true);
        ShoePanel.SetActive(true);
        buttons[2].SetActive(false);
        crosses[1].SetActive(true);
    }
    public void OnAccessoriesClick()
    {
        Reset();
        dismissPanel.SetActive(true);
        AccessoryPanel.SetActive(true);
        rotatePanel.SetActive(false);
        buttons[3].SetActive(false);
        crosses[2].SetActive(true);
    }
    public void OnPatternClick()
    {
        Reset();
        dismissPanel.SetActive(true);
        TexturePanel.SetActive(true);
        buttons[4].SetActive(false);
        crosses[3].SetActive(true);
    }
    public void OnRotateClick()
    {
        Reset();
        rotatePanel.SetActive(true);
        buttons[6].SetActive(false);
        crosses[4].SetActive(true);

    }
    public void OnCrossClick()
    {
        Reset();
        dismissPanel.SetActive(false);
        for (int i = 0; i < buttons.Length; i++)
        {
            buttons[i].SetActive(true);
        }
        for (int i = 0; i < crosses.Length; i++)
        {
            crosses[i].SetActive(false);
        }
    }
    public void OnSwitchCameraClick() {
        if (back)
        {
            UnityEngine.SceneManagement.SceneManager.LoadScene("FrontAugmentation");
        }
        else
        {
            UnityEngine.SceneManagement.SceneManager.LoadScene("BackAugmentation");
        }
    }
    Text timerText;
    public void OnCaptureClick() {
        MainPanel.SetActive(false);
        DressPanel.SetActive(false);
        TexturePanel.SetActive(false);
        ShoePanel.SetActive(false);
        AccessoryPanel.SetActive(false);
        rotatePanel.SetActive(false);
        if (back)
        {
            ground.SetActive(false);
            
            StartCoroutine(TakeScreenshotAndSave());
        }
        else {
            timer.SetActive(true);
            timer.GetComponent<Animator>().SetTrigger("Show");
            timerText = timer.GetComponentInChildren<Text>();
            timerText.text = 3 + "";
            Invoke("CaptureAfterWait", 6f);
            InvokeRepeating("ChangeVal", 2f,2f);
        }
    }
    void ChangeVal() {
        if (int.Parse(GameObject.Find("TIMER").GetComponentInChildren<Text>().text) > 1)
        {
            GameObject.Find("TIMER").GetComponentInChildren<Text>().text = int.Parse(GameObject.Find("TIMER").GetComponentInChildren<Text>().text) - 1 + "";
        }
        else {
            CancelInvoke("ChangeVal");
            print("hi");
        }
    }
    void CaptureAfterWait() {
        CancelInvoke("ChangeVal");
        timer.SetActive(false);
        StartCoroutine(TakeScreenshotAndSave());
        timerText.text = 3 + "";
    }
    private IEnumerator TakeScreenshotAndSave()
    {
        yield return new WaitForEndOfFrame();

        Texture2D ss = new Texture2D(Screen.width, Screen.height, TextureFormat.RGB24, false);
        ss.ReadPixels(new Rect(0, 0, Screen.width, Screen.height), 0, 0);
        ss.Apply();

        Debug.Log("Permission result: " + NativeGallery.SaveImageToGallery(ss, "StyleMe", "style.png"));

        Destroy(ss);
        MainPanel.SetActive(true);
        if (back) {
            ground.SetActive(true);
        }
    }
}
