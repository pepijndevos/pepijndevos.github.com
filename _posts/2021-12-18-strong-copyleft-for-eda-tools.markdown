---
layout: post
title: Strong copyleft for EDA tools
categories:
- license
- opensource
---

The release of the open source Skywater 130nm PDK seems to have started a flurry of activity around open source EDA tools, with several companies trying to form a business around an open source suite of chip design tools of some form. Of course this raises the question of what business model you're going for.

As far as the licensing aspect is concerned, there are things to be said for the easier commercial adoption of permissive licensed tools and for copyleft encouraging the contribution of modifications, and enabling dual-license models.

A well known example of the latter is Qt, which is released under the GPL, so you can use it freely for open source projects, but if you want to develop a proprietary application that you don't want to release under the GPL, you'll have to get a commercial license.

Would such a model work for EDA tools? Unlike Qt your users are not writing software, they are making a chip, so GPL doesn't really cover that. I'm very much not a lawyer, but my reading of the [CERN Open Hardware License](https://cern-ohl.web.cern.ch/home) is that the strongly reciprocal one would do exactly that. Let me explain why.

The [CERN OHL version 2, An Introduction and Explanation](https://ohwr.org/project/cernohl/wikis/uploads/0be6f561d2b4a686c5765c74be32daf9/CERN_OHL_rationale.pdf) is a great place to get an understanding what problem it is trying to solve, and spoiler, it's not EDA tools. It basically tries to solve issues with using GPL for hardware: hardware is not free as in beer, and not all IP blocks and components are open source.

A centerpiece of the license is that of an "available component" which can be either an open source thing, or something anyone can obtain with sufficient documentation to reproduce your thing. The goal is basically that if I write some HDL, the copyleft aspect applies to a PCB made with my HDL on it, but you don't need to provide the source code for a resistor, anyone can just buy a resistor and download its datasheet. It's an available component.

But when you squint a bit, the HDL->PCB copyleft mechanism is kinda similar to an EDA software->chip copyleft mechanism. But lawyers don't "squint a bit" so we'll have to get into the weeds to see if the exact wording makes sense for EDA software.

The excerpts below come from the full [CERN Open Hardware Licence Version 2 - Strongly Reciprocal](https://ohwr.org/cern_ohl_s_v2.txt)

>   2.1 This Licence governs the use, copying, modification, Conveying
      of Covered Source and Products, and the Making of Products. By
      exercising any right granted under this Licence, You irrevocably
      accept these terms and conditions.

The interesting bit here is the Making of Products. We'll get back to the definitions in a minute, but it's important to note that the license does not just cover modifying the source, but also using it to Make Products. Such as using EDA tools to make a chip, if these fit the definition.

Section 3 is typical copyleft stuff about modifying the Covered Source, but section 4 reads

> 4 Making and Conveying Products
>
> You may Make Products, and/or Convey them, provided that You either
provide each recipient with a copy of the Complete Source or ensure
that each recipient is notified of the Source Location of the Complete
Source. That Complete Source is Covered Source, and You must
accordingly satisfy Your obligations set out in subsection 3.3. If
specified in a Notice, the Product must visibly and securely display
the Source Location on it or its packaging or documentation in the
manner specified in that Notice.

So you can Make Products if you give the recipient the Complete Source, and the Complete Source is Covered Source. Let's have a look at their definition.

>  1.3 'Source' means information such as design materials or digital
      code which can be applied to Make or test a Product or to
      prepare a Product for use, Conveyance or sale, regardless of its
      medium or how it is expressed. It may include Notices.

So Source can be code, HDL, Spice, GDS, or any other design material.

>  1.4 'Covered Source' means Source that is explicitly made available
      under this Licence.

This includes the source of our EDA tools, and the Complete Source.

>  1.5 'Product' means any device, component, work or physical object,
      whether in finished or intermediate form, arising from the use,
      application or processing of Covered Source.

Clearly a chip is a device, that arises from the use of Covered Source, the EDA tools.
But a GDS, bitstream, or netlist are also a work (in intermediate form) that arise from application of the Covered Source.

>  1.6 'Make' means to create or configure something, whether by
      manufacture, assembly, compiling, loading or applying Covered
      Source or another Product or otherwise.

You do a thing to another thing. Specifically by applying Covered Source (EDA tools) to load or compile some source.

>  1.8 'Complete Source' means the set of all Source necessary to Make
      a Product, in the preferred form for making modifications,
      including necessary installation and interfacing information
      both for the Product, and for any included Available Components.
      If the format is proprietary, it must also be made available in
      a format (if the proprietary tool can create it) which is
      viewable with a tool available to potential licensees and
      licensed under a licence approved by the Free Software
      Foundation or the Open Source Initiative. Complete Source need
      not include the Source of any Available Component, provided that
      You include in the Complete Source sufficient information to
      enable a recipient to Make or source and use the Available
      Component to Make the Product.

So all the stuff needed to make the chip basically, EDA tools as well as design files. Either as Source or Available Components.

With those definitions in mind, let's look at section 4 again. Paraphrasing, you can Make a Product if you provide the recipient with the Complete Source. Expanding that, it says you can apply the Covered Source (EDA tools) to make a device (chip) or intermediate work (netlist, GDS), provided you give the recipient all the source files or available components specs necessary to do so.

Long story short, my amateur reading of all this is that if you release your EDA tools under CERN-OHL-S and your users make a chip with it, they have to make the design files available, unless they obtain a commercial license from you.