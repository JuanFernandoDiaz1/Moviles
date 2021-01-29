using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerController : MonoBehaviour
{
    private float speed = 15f;

    Animator anim;
    Rigidbody2D rigb2d;

    private Vector2 mov2;
    
    // Start is called before the first frame update
    void Start()
    {
        anim = GetComponent<Animator>();
        rigb2d = GetComponent<Rigidbody2D>();
    }

    // Update is called once per frame
    void Update()
    {
       /*Vector3 mov;
       mov = new Vector3(Input.GetAxisRaw("Horizontal"), Input.GetAxisRaw("Vertical"), 0);
       transform.position = Vector3.MoveTowards(transform.position, transform.position + mov, speed * Time.deltaTime);
       */
       
       mov2 = new Vector2(Input.GetAxisRaw("Horizontal"), Input.GetAxisRaw("Vertical"));
       anim.SetFloat("movX", mov2.x);
       anim.SetFloat("movY", mov2.y);
    }

    private void FixedUpdate()
    {
        rigb2d.MovePosition(rigb2d.position + mov2 * speed * Time.deltaTime);
    }
}
