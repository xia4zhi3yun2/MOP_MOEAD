function d=drawBox(lx, ly, lz, ux, uy, uz,cl)

Vert=[lx ly lz ;
      ux ly lz ;
      ux uy lz ;
      lx uy lz ;
      lx ly uz ;
      ux ly uz ;
      ux uy uz ;
      lx uy uz];
 Faces = [ ...
        1 2 6 5 ;
        2 3 7 6 ;
        3 4 8 7 ;
        4 1 5 8 ;
        1 2 3 4 ;
        5 6 7 8 ];
    
    ptch.Vertices = Vert;
    ptch.Faces = Faces;
    ptch.FaceVertexCData = bone(6);
    ptch.FaceColor =cl;
    %ptch.FaceColor = [0.9,0.9,0.9];
    %ptch.FaceColor = 'red';
   
   
    d=patch(ptch);
    %set(d,'FaceAlpha',0.5);
    
    view(3); axis square;
