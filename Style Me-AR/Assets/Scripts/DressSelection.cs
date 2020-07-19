using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class DressSelection : MonoBehaviour
{
    public GameObject currentSelectedItem;
    public List<GameObject> dresses;
    public List<GameObject> shoes;
    public List<Material> materials;
    public List<GameObject> accessories;
    public Slider slider;
    public Button TexturesButton;
    public GameObject parent;
    private void Start()
    {
        for (int i = 0; i < dresses.Count; i++) {
            dresses[i].GetComponent<Lean.Touch.LeanTranslate>().enabled = false;
            dresses[i].GetComponent<Lean.Touch.LeanScale>().enabled = false;
            dresses[i].SetActive(false);
        }
        for (int i = 0; i < shoes.Count; i++)
        {

            shoes[i].GetComponent<Lean.Touch.LeanTranslate>().enabled = false;
            shoes[i].GetComponent<Lean.Touch.LeanScale>().enabled = false;
            shoes[i].SetActive(false);
        }
        for (int i = 0; i < accessories.Count; i++)
        {
            accessories[i].SetActive(false);
            accessories[i].GetComponent<Lean.Touch.LeanTranslate>().enabled = false;
            accessories[i].GetComponent<Lean.Touch.LeanScale>().enabled = false;
        }
    }
    public void DeselectAll() {
        for (int i = 0; i < dresses.Count; i++)
        {
            dresses[i].GetComponent<Lean.Touch.LeanTranslate>().enabled = false;
            dresses[i].GetComponent<Lean.Touch.LeanScale>().enabled = false;
        }
        for (int i = 0; i < shoes.Count; i++)
        {

            shoes[i].GetComponent<Lean.Touch.LeanTranslate>().enabled = false;
            shoes[i].GetComponent<Lean.Touch.LeanScale>().enabled = false;
        }
        for (int i = 0; i < accessories.Count; i++)
        {
            accessories[i].GetComponent<Lean.Touch.LeanTranslate>().enabled = false;
            accessories[i].GetComponent<Lean.Touch.LeanScale>().enabled = false;
        }
    }
    public void OnSliderValueChanged() {
        parent.transform.rotation = Quaternion.Euler(0,slider.value,0);
    }
    private void Update()
    {
        if (currentSelectedItem==shoes[0] || currentSelectedItem == shoes[1])
        {
            TexturesButton.interactable = false;
        }
        else {
            TexturesButton.interactable = true;
        }
    }
    public void OnSelectDress(int index) {
        if (!dresses[index].activeInHierarchy)
        {
            DeselectAll();
            for (int i = 0; i < dresses.Count; i++)
            {
                dresses[i].SetActive(false);
            }
            dresses[index].SetActive(true);
            dresses[index].GetComponent<Lean.Touch.LeanTranslate>().enabled = true;
            dresses[index].GetComponent<Lean.Touch.LeanScale>().enabled = true;
            currentSelectedItem = dresses[index];
        }
        else
        {
            for (int i = 0; i < dresses.Count; i++)
            {
                dresses[i].SetActive(false);
                dresses[i].GetComponent<Lean.Touch.LeanTranslate>().enabled = false;
                dresses[i].GetComponent<Lean.Touch.LeanScale>().enabled = false;
            }
        }

    }
    public void OnSelectShoes(int index) {

        if (!shoes[index].activeInHierarchy)
        {
            DeselectAll();
            for (int i = 0; i < shoes.Count; i++)
            {
                shoes[i].SetActive(false);
            }
            shoes[index].SetActive(true);
            shoes[index].GetComponent<Lean.Touch.LeanTranslate>().enabled = true;
            shoes[index].GetComponent<Lean.Touch.LeanScale>().enabled = true;
            currentSelectedItem = shoes[index];
        }
        else {
            for (int i = 0; i < shoes.Count; i++)
            {
                shoes[i].SetActive(false);
                shoes[i].GetComponent<Lean.Touch.LeanTranslate>().enabled = false;
                shoes[i].GetComponent<Lean.Touch.LeanScale>().enabled = false;
            }
        }
    }
    public void OnSelectAccessory(int index) {
        if (!accessories[index].activeInHierarchy)
        {
            DeselectAll();
            for (int i = 0; i < accessories.Count; i++)
            {
                accessories[i].SetActive(false);
            }
            accessories[index].SetActive(true);
            accessories[index].GetComponent<Lean.Touch.LeanTranslate>().enabled = true;
            accessories[index].GetComponent<Lean.Touch.LeanScale>().enabled = true;
            currentSelectedItem = accessories[index];
        }
        else {
            for (int i = 0; i < accessories.Count; i++)
            {
                accessories[i].SetActive(false);
                accessories[i].GetComponent<Lean.Touch.LeanTranslate>().enabled = false;
                accessories[i].GetComponent<Lean.Touch.LeanScale>().enabled = false;
            }
        }
    }
    public void OnSelectTexture(int index) {
        MeshRenderer[] meshes = currentSelectedItem.GetComponentsInChildren<MeshRenderer>();
        if (currentSelectedItem == dresses[0])
        {
            meshes[10].material = materials[index];
            meshes[19].material = materials[index];
        }
        else if (currentSelectedItem == accessories[1])
        {
            for (int i = 2; i < meshes.Length; i++)
            {

                meshes[i].material = materials[index];
            }
        }
        else {
            for (int i = 0; i < meshes.Length; i++)
            {

                meshes[i].material = materials[index];
            }
        }
    }
}
