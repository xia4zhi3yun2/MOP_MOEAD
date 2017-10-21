function drawGreyBoxes(M)
    [k,s]=size(M)
    A=M(:,1);
    B=M(:,2);
    C=M(:,3);
    %scatter3(A,B,C,10,'filled');
    xlabel('f_1');
    ylabel('f_2');
    zlabel('f_3');
    for (i=1:k)
        drawBox(M(i,1), M(i,2), M(i,3),0, 0, 0,[0.9,0.9,0.9])    
    end
    view([127,45])
    set(gca,'FontSize',16);
    pause(1);
    set(gca,'FontSize',16);
    
   
    

